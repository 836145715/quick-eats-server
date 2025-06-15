package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.enums.ErrorCodeEnum;
import com.zmx.common.exception.BusinessException;
import com.zmx.common.properties.JwtUserProperties;
import com.zmx.common.response.Result;
import com.zmx.common.utils.BaseContext;
import com.zmx.common.utils.JwtUtils;
import com.zmx.quickpojo.dto.WechatApiResponseDTO;
import com.zmx.quickpojo.dto.WechatLoginReqDTO;
import com.zmx.quickpojo.entity.User;
import com.zmx.quickpojo.vo.UserInfoVO;
import com.zmx.quickpojo.vo.WechatLoginRspVO;
import com.zmx.quickserver.mapper.UserMapper;
import com.zmx.quickserver.service.UserService;
import com.zmx.quickserver.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户Service实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private JwtUserProperties jwtUserProperties;

    /**
     * 微信小程序登录
     *
     * @param loginDTO 登录请求参数
     * @return 登录结果
     */
    @Override
    public Result<WechatLoginRspVO> wechatLogin(WechatLoginReqDTO loginDTO) {
        log.info("微信小程序登录，code: {}", loginDTO.getCode());

        // 1. 调用微信API获取openid
        WechatApiResponseDTO wechatResponse = wechatService.getWechatUserInfo(loginDTO.getCode());
        if (wechatResponse == null || wechatResponse.getOpenid() == null) {
            throw new BusinessException(ErrorCodeEnum.WECHAT_LOGIN_FAILED);
        }

        String openid = wechatResponse.getOpenid();
        log.info("获取到用户openid: {}", openid);

        // 2. 根据openid查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getOpenid, openid);
        User user = this.getOne(queryWrapper);

        // 3. 如果用户不存在，创建新用户
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setName("微信用户");
            user.setCreateTime(LocalDateTime.now());

            boolean saved = this.save(user);
            if (!saved) {
                log.error("创建用户失败，openid: {}", openid);
                throw new BusinessException(ErrorCodeEnum.USER_CREATE_FAILED);
            }
            log.info("创建新用户成功，用户ID: {}, openid: {}", user.getId(), openid);
        } else {
            log.info("用户已存在，用户ID: {}, openid: {}", user.getId(), openid);
        }

        // 4. 生成JWT token
        String token = JwtUtils.configure(jwtUserProperties).generateToken(user.getId());

        // 5. 构建响应对象
        WechatLoginRspVO response = WechatLoginRspVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .name(user.getName())
                .avatar(user.getAvatar())
                .token(token)
                .build();

        log.info("微信小程序登录成功，用户ID: {}", user.getId());
        return Result.success(response);
    }

    @Override
    public UserInfoVO getUserInfo() {
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_EXIST);
        }

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setName(user.getName());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvatar(user.getAvatar());

        return userInfo;
    }
}
package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.WechatLoginReqDTO;
import com.zmx.quickpojo.entity.User;
import com.zmx.quickpojo.vo.UserInfoVO;
import com.zmx.quickpojo.vo.WechatLoginRspVO;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 微信小程序登录
     *
     * @param loginDTO 登录请求参数
     * @return 登录结果
     */
    Result<WechatLoginRspVO> wechatLogin(WechatLoginReqDTO loginDTO);

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    UserInfoVO getUserInfo();
}
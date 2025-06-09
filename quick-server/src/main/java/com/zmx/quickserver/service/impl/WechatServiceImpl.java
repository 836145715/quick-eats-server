package com.zmx.quickserver.service.impl;

import com.zmx.common.properties.WechatProperties;
import com.zmx.common.utils.HttpUtils;
import com.zmx.quickpojo.dto.WechatApiResponseDTO;
import com.zmx.quickserver.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信服务实现类
 */
@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private WechatProperties wechatProperties;

    /**
     * 微信小程序登录API地址
     */
    private static final String WECHAT_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 通过code获取微信用户信息
     *
     * @param code 微信小程序登录凭证
     * @return 微信API响应
     */
    @Override
    public WechatApiResponseDTO getWechatUserInfo(String code) {
        log.info("调用微信API获取用户信息, code: {}", code);

        // 构建请求URL
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                WECHAT_LOGIN_URL,
                wechatProperties.getAppid(),
                wechatProperties.getSecret(),
                code);

        // 发送HTTP请求
        WechatApiResponseDTO response = HttpUtils.doGet(url, WechatApiResponseDTO.class);

        if (response == null) {
            log.error("调用微信API失败，响应为空");
            return null;
        }

        if (response.getErrcode() != null && response.getErrcode() != 0) {
            log.error("调用微信API失败，错误码: {}, 错误信息: {}", response.getErrcode(), response.getErrmsg());
            return null;
        }

        log.info("调用微信API成功，openid: {}", response.getOpenid());
        return response;
    }
}

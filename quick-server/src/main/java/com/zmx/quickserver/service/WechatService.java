package com.zmx.quickserver.service;

import com.zmx.quickpojo.dto.WechatApiResponseDTO;

/**
 * 微信服务接口
 */
public interface WechatService {

    /**
     * 通过code获取微信用户信息
     *
     * @param code 微信小程序登录凭证
     * @return 微信API响应
     */
    WechatApiResponseDTO getWechatUserInfo(String code);
}

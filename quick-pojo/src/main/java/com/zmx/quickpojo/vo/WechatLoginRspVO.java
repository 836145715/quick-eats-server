package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信小程序登录响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "微信小程序登录响应VO")
public class WechatLoginRspVO {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long id;

    /**
     * 用户openid
     */
    @Schema(description = "用户openid", example = "oGZUI0egBJY1zhBLw2KdUr8-pyzw")
    private String openid;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "微信用户")
    private String name;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像", example = "https://thirdwx.qlogo.cn/mmopen/...")
    private String avatar;

    /**
     * JWT令牌
     */
    @Schema(description = "JWT令牌", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}

package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信小程序登录请求DTO
 */
@Data
@Schema(description = "微信小程序登录请求DTO")
public class WechatLoginReqDTO {

    /**
     * 微信小程序登录凭证code
     */
    @NotBlank(message = "登录凭证不能为空")
    @Schema(description = "微信小程序登录凭证code", required = true, example = "0a1b2c3d4e5f6g7h8i9j")
    private String code;
}

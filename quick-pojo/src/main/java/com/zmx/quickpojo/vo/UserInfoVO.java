package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息响应VO
 */
@Data
@Schema(description = "用户信息响应VO")
public class UserInfoVO {

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String name;

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13888888888")
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

}
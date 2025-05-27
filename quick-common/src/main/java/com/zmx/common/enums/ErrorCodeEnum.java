package com.zmx.common.enums;

import lombok.Getter;

/**
 * 错误码枚举类
 */
@Getter
public enum ErrorCodeEnum {

    // 通用错误码
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统异常，请联系管理员"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),

    // 用户相关错误码 (11xxx)
    USER_NOT_EXIST(11001, "用户名不存在"),
    PASSWORD_ERROR(11002, "密码错误"),
    ACCOUNT_DISABLED(11003, "账号已禁用"),

    // 其他业务错误码可以在此处添加
    ;

    private final Integer code;
    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
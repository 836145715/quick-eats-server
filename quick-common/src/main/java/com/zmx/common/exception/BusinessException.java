package com.zmx.common.exception;

import com.zmx.common.enums.ErrorCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 通过错误枚举构造业务异常
     *
     * @param errorCodeEnum 错误枚举
     */
    public BusinessException(ErrorCodeEnum errorCodeEnum) {
        super(errorCodeEnum.getMessage());
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
    }

    /**
     * 通过错误枚举和自定义消息构造业务异常
     *
     * @param errorCodeEnum 错误枚举
     * @param message       自定义消息
     */
    public BusinessException(ErrorCodeEnum errorCodeEnum, String message) {
        super(message);
        this.code = errorCodeEnum.getCode();
        this.message = message;
    }

    /**
     * 通过错误码和消息构造业务异常
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
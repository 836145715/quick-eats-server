package com.zmx.common.response;

import com.zmx.common.enums.ErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应结果
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功结果
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = ErrorCodeEnum.SUCCESS.getCode();
        result.msg = ErrorCodeEnum.SUCCESS.getMessage();
        return result;
    }

    /**
     * 成功结果（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = ErrorCodeEnum.SUCCESS.getCode();
        result.msg = ErrorCodeEnum.SUCCESS.getMessage();
        result.data = data;
        return result;
    }

    /**
     * 系统错误
     */
    public static <T> Result<T> error() {
        Result<T> result = new Result<>();
        result.code = ErrorCodeEnum.SYSTEM_ERROR.getCode();
        result.msg = ErrorCodeEnum.SYSTEM_ERROR.getMessage();
        return result;
    }

    /**
     * 失败结果
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = ErrorCodeEnum.SYSTEM_ERROR.getCode();
        result.msg = msg;
        return result;
    }

    /**
     * 失败结果（带状态码）
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    /**
     * 通过错误枚举构造失败结果
     */
    public static <T> Result<T> error(ErrorCodeEnum errorCodeEnum) {
        Result<T> result = new Result<>();
        result.code = errorCodeEnum.getCode();
        result.msg = errorCodeEnum.getMessage();
        return result;
    }

    /**
     * 通过错误枚举和自定义消息构造失败结果
     */
    public static <T> Result<T> error(ErrorCodeEnum errorCodeEnum, String message) {
        Result<T> result = new Result<>();
        result.code = errorCodeEnum.getCode();
        result.msg = message;
        return result;
    }
}
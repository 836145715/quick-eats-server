package com.zmx.common.response;

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
        result.code = 1;
        result.msg = "操作成功";
        return result;
    }

    /**
     * 成功结果（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = "操作成功";
        result.data = data;
        return result;
    }

    /**
     * 失败结果
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.code = 0;
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
}
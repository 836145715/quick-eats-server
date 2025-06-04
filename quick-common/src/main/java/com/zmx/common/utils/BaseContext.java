package com.zmx.common.utils;

/**
 * 基于ThreadLocal封装的工具类，用于存储和获取当前登录用户ID
 */
public class BaseContext {

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前登录用户ID
     * 
     * @param id 用户ID
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前登录用户ID
     * 
     * @return 用户ID
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 清除当前登录用户ID
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }
}
package com.zmx.quickserver.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zmx.common.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器，用于自动填充创建时间、更新时间、创建人、更新人
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入操作自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        log.info("当前线程ID：{}", Thread.currentThread().getId());

        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("createTime", now, metaObject);
        this.setFieldValByName("updateTime", now, metaObject);

        // 设置创建人和更新人
        Long currentId = BaseContext.getCurrentId();
        log.info("当前登录用户ID：{}", currentId);

        this.setFieldValByName("createUser", currentId, metaObject);
        this.setFieldValByName("updateUser", currentId, metaObject);
    }

    /**
     * 更新操作自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info("当前线程ID：{}", Thread.currentThread().getId());

        // 设置更新时间
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);

        // 设置更新人
        Long currentId = BaseContext.getCurrentId();
        log.info("当前登录用户ID：{}", currentId);

        this.setFieldValByName("updateUser", currentId, metaObject);
    }
}
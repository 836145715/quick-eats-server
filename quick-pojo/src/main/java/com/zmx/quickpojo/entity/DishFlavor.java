package com.zmx.quickpojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.Data;

import java.util.List;

/**
 * 菜品口味实体类
 */
@Data
@TableName(value = "dish_flavor",autoResultMap = true)
public class DishFlavor {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long dishId;

    private String name;

    @TableField(value = "value",typeHandler = JacksonTypeHandler.class)
    private List<String> value;
}
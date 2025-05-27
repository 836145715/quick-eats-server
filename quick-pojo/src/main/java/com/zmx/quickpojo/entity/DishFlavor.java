package com.zmx.quickpojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 菜品口味实体类
 */
@Data
@TableName("dish_flavor")
public class DishFlavor {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long dishId;

    private String name;

    private String value;
}
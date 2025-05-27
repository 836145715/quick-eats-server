package com.zmx.quickpojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 套餐菜品关系实体类
 */
@Data
@TableName("setmeal_dish")
public class SetmealDish {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long setmealId;

    private Long dishId;

    private String name;

    private BigDecimal price;

    private Integer copies;
}
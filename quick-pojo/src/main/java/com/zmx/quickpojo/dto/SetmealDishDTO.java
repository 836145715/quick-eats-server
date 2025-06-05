package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐菜品关联DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "套餐菜品关联DTO")
public class SetmealDishDTO implements Serializable {

    /**
     * 菜品ID
     */
    @Schema(description = "菜品ID")
    private Long dishId;

    /**
     * 菜品名称
     */
    @Schema(description = "菜品名称")
    private String name;

    /**
     * 菜品价格
     */
    @Schema(description = "菜品价格")
    private BigDecimal price;

    /**
     * 份数
     */
    @Schema(description = "份数")
    private Integer copies;
}
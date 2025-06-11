package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 购物车请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "购物车请求DTO")
public class ShoppingCartDTO implements Serializable {

    /**
     * 菜品ID（添加菜品时必填）
     */
    @Schema(description = "菜品ID", example = "1")
    private Long dishId;

    /**
     * 套餐ID（添加套餐时必填）
     */
    @Schema(description = "套餐ID", example = "1")
    private Long setmealId;

    /**
     * 菜品口味（添加菜品时可选）
     */
    @Schema(description = "菜品口味", example = "微辣")
    private String dishFlavor;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    @Positive(message = "商品数量必须大于0")
    @Schema(description = "商品数量", required = true, example = "1")
    private Integer number;
}

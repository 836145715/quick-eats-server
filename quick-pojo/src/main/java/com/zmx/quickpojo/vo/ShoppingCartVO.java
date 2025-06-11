package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "购物车响应VO")
public class ShoppingCartVO implements Serializable {

    /**
     * 购物车ID
     */
    @Schema(description = "购物车ID", example = "1")
    private Long id;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "宫保鸡丁")
    private String name;

    /**
     * 商品图片
     */
    @Schema(description = "商品图片", example = "http://example.com/image.jpg")
    private String image;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 菜品ID
     */
    @Schema(description = "菜品ID", example = "1")
    private Long dishId;

    /**
     * 套餐ID
     */
    @Schema(description = "套餐ID", example = "1")
    private Long setmealId;

    /**
     * 菜品口味
     */
    @Schema(description = "菜品口味", example = "微辣")
    private String dishFlavor;

    /**
     * 商品数量
     */
    @Schema(description = "商品数量", example = "2")
    private Integer number;

    /**
     * 商品金额
     */
    @Schema(description = "商品金额", example = "76.00")
    private BigDecimal amount;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

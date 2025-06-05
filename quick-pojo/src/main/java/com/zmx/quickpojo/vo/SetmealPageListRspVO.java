package com.zmx.quickpojo.vo;

import com.zmx.quickpojo.dto.SetmealDishDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 套餐分页查询响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "套餐分页查询响应VO")
public class SetmealPageListRspVO implements Serializable {

    /**
     * 套餐ID
     */
    @Schema(description = "套餐ID")
    private Long id;

    /**
     * 套餐名称
     */
    @Schema(description = "套餐名称")
    private String name;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String categoryName;

    /**
     * 套餐价格
     */
    @Schema(description = "套餐价格")
    private BigDecimal price;

    /**
     * 套餐图片
     */
    @Schema(description = "套餐图片")
    private String image;

    /**
     * 套餐描述
     */
    @Schema(description = "套餐描述")
    private String description;

    /**
     * 套餐状态
     */
    @Schema(description = "套餐状态：1起售，0停售")
    private Integer status;

    /**
     * 套餐包含的菜品列表
     */
    @Schema(description = "套餐包含的菜品列表")
    private List<SetmealDishDTO> setmealDishes;
}
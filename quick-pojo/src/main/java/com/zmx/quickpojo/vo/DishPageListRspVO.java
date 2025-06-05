package com.zmx.quickpojo.vo;

import com.zmx.quickpojo.dto.DishFlavorDTO;
import com.zmx.quickpojo.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 菜品分页查询响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜品分页查询响应VO")
public class DishPageListRspVO implements Serializable {

    /**
     * 菜品ID
     */
    @Schema(description = "菜品ID")
    private Long id;

    /**
     * 菜品名称
     */
    @Schema(description = "菜品名称")
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
     * 菜品价格
     */
    @Schema(description = "菜品价格")
    private BigDecimal price;

    /**
     * 菜品图片
     */
    @Schema(description = "菜品图片")
    private String image;

    /**
     * 菜品描述
     */
    @Schema(description = "菜品描述")
    private String description;

    /**
     * 菜品状态
     */
    @Schema(description = "菜品状态：1起售，0停售")
    private Integer status;


    @Schema(description = "菜品口味列表")
    private List<DishFlavorDTO> flavors;

}
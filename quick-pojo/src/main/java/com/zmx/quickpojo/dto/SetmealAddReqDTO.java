package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 套餐添加请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "套餐添加请求DTO")
public class SetmealAddReqDTO implements Serializable {

    /**
     * 套餐ID（新增时不需要传，修改时必传）
     */
    @Schema(description = "套餐ID", example = "1")
    private Long id;

    /**
     * 套餐名称
     */
    @NotBlank(message = "套餐名称不能为空")
    @Schema(description = "套餐名称", required = true, example = "商务套餐A")
    private String name;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", required = true, example = "1")
    private Long categoryId;

    /**
     * 套餐价格
     */
    @NotNull(message = "套餐价格不能为空")
    @PositiveOrZero(message = "套餐价格必须大于等于0")
    @Schema(description = "套餐价格", required = true, example = "88.00")
    private BigDecimal price;

    /**
     * 套餐图片
     */
    @Schema(description = "套餐图片", example = "http://example.com/image.jpg")
    private String image;

    /**
     * 套餐描述
     */
    @Schema(description = "套餐描述", example = "商务套餐A，包含多种菜品")
    private String description;

    /**
     * 套餐状态（1:启用，0:禁用）
     */
    @Schema(description = "套餐状态（1:启用，0:禁用）", example = "1")
    private Integer status;

    /**
     * 套餐菜品关联信息列表
     */
    @NotEmpty(message = "套餐菜品不能为空")
    @Schema(description = "套餐菜品关联信息列表")
    private List<SetmealDishDTO> setmealDishes;
}
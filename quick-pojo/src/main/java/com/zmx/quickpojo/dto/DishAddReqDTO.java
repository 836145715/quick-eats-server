package com.zmx.quickpojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
 * 菜品添加请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "菜品添加请求DTO")
public class DishAddReqDTO implements Serializable {

    /**
     * 菜品ID（新增时不需要传，修改时必传）
     */
    @Schema(description = "菜品ID", example = "1")
    private Long id;

    /**
     * 菜品名称
     */
    @NotBlank(message = "菜品名称不能为空")
    @Schema(description = "菜品名称", required = true, example = "宫保鸡丁")
    private String name;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", required = true, example = "1")
    private Long categoryId;

    /**
     * 菜品价格
     */
    @NotNull(message = "菜品价格不能为空")
    @PositiveOrZero(message = "菜品价格必须大于等于0")
    @Schema(description = "菜品价格", required = true, example = "38.00")
    private BigDecimal price;

    /**
     * 菜品图片
     */
    @Schema(description = "菜品图片", example = "http://example.com/image.jpg")
    private String image;

    /**
     * 菜品描述
     */
    @Schema(description = "菜品描述", example = "宫保鸡丁，是一道闻名中外的传统名菜")
    private String description;

    /**
     * 菜品状态（1:启用，0:禁用）
     */
    @Schema(description = "菜品状态（1:启用，0:禁用）", example = "1")
    private Integer status;

    /**
     * 菜品口味列表
     */
    @Schema(description = "菜品口味列表")
    private List<DishFlavorDTO> flavors;
}
package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类添加/更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类添加/更新请求DTO")
public class CategoryAddReqDTO {

    /**
     * 分类ID，更新时必填
     */
    @Schema(description = "分类ID，更新时必填")
    private Long id;

    /**
     * 分类类型 1 菜品分类 2 套餐分类
     */
    @NotNull(message = "分类类型不能为空")
    @Schema(description = "分类类型：1 菜品分类，2 套餐分类", required = true)
    private Integer type;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称", required = true)
    private String name;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @Schema(description = "排序", required = true)
    private Integer sort;
}
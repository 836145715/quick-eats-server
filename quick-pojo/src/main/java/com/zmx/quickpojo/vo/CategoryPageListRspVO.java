package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类分页查询响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类分页查询响应VO")
public class CategoryPageListRspVO {

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    private Long id;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;

    /**
     * 分类类型
     */
    @Schema(description = "分类类型：1 菜品分类，2 套餐分类")
    private Integer type;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 状态
     */
    @Schema(description = "状态：1 启用，0 禁用")
    private Integer status;
}
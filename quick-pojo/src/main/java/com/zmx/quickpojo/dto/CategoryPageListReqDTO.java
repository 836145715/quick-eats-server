package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类分页查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类分页查询请求DTO")
public class CategoryPageListReqDTO {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", defaultValue = "1")
    private Integer current = 1;

    /**
     * 每页记录数
     */
    @Schema(description = "每页记录数", defaultValue = "10")
    private Integer size = 10;

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
}
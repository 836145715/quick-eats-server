package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品分页查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜品分页查询请求DTO")
public class DishPageListReqDTO implements Serializable {

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
     * 菜品状态
     */
    @Schema(description = "菜品状态：1起售，0停售")
    private Integer status;
}
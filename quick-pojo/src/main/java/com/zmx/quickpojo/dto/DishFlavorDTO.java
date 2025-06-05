package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 菜品口味DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜品口味DTO")
public class DishFlavorDTO implements Serializable {

    /**
     * 口味名称
     */
    @Schema(description = "口味名称", example = "辣度")
    private String name;

    /**
     * 口味值
     */
    @Schema(description = "口味值", example = "[\"不辣\", \"微辣\", \"中辣\", \"重辣\"]")
    private List<String> value;
}
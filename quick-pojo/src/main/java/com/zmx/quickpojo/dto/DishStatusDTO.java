package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品状态DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜品状态DTO")
public class DishStatusDTO implements Serializable {

    /**
     * 菜品ID
     */
    @NotNull(message = "菜品ID不能为空")
    @Schema(description = "菜品ID", required = true)
    private Long id;

    /**
     * 菜品状态（1:起售，0:停售）
     */
    @NotNull(message = "菜品状态不能为空")
    @Schema(description = "菜品状态（1:起售，0:停售）", required = true)
    private Integer status;
}
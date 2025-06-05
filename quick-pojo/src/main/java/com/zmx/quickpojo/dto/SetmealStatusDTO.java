package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 套餐状态DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "套餐状态DTO")
public class SetmealStatusDTO implements Serializable {

    /**
     * 套餐ID
     */
    @NotNull(message = "套餐ID不能为空")
    @Schema(description = "套餐ID", required = true)
    private Long id;

    /**
     * 套餐状态（1:起售，0:停售）
     */
    @NotNull(message = "套餐状态不能为空")
    @Schema(description = "套餐状态（1:起售，0:停售）", required = true)
    private Integer status;
}
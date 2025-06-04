package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类状态DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分类状态DTO")
public class CategoryStatusDTO {

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", required = true)
    private Long id;

    /**
     * 分类状态（1:启用，0:禁用）
     */
    @NotNull(message = "分类状态不能为空")
    @Schema(description = "分类状态（1:启用，0:禁用）", required = true)
    private Integer status;
}
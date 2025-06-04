package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 员工状态DTO
 */
@Data
@Schema(description = "员工状态DTO")
public class EmployeeStatusDTO {

    @NotNull(message = "员工ID不能为空")
    @Schema(description = "员工ID", required = true)
    private Long id;

    @NotNull(message = "员工状态不能为空")
    @Schema(description = "员工状态（1:启用，0:禁用）", required = true)
    private Integer status;
}
package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "员工更新DTO")
public class EmployeeUpdateReqDTO {

    /**
     * 员工ID，更新时必填
     */
    @Schema(description = "员工ID，更新时必填")
    private Long id;

    /**
     * 姓名
     */
    @Schema(description = "姓名", required = true)
    private String name;

    /**
     * 用户名
     */
    @Schema(description = "用户名", required = true)
    private String username;

    /**
     * 手机号
     */
    @Schema(description = "手机号", required = true)
    private String phone;

    /**
     * 状态
     */
    @Schema(description = "状态", required = true)
    private Integer status;

    /**
     * 性别
     */
    @Schema(description = "性别", required = true)
    private String sex;

    /**
     * 身份证号
     */
    @Schema(description = "身份证号", required = true)
    private String idNumber;
}

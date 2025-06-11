package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址验证请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "地址验证请求DTO")
public class AddressValidateReqDTO implements Serializable {

    /**
     * 省份名称
     */
    @NotBlank(message = "省份不能为空")
    @Schema(description = "省份名称", required = true, example = "北京市")
    private String provinceName;

    /**
     * 城市名称
     */
    @NotBlank(message = "城市不能为空")
    @Schema(description = "城市名称", required = true, example = "北京市")
    private String cityName;

    /**
     * 区县名称
     */
    @NotBlank(message = "区县不能为空")
    @Schema(description = "区县名称", required = true, example = "东城区")
    private String districtName;

    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Schema(description = "详细地址", required = true, example = "王府井大街1号")
    private String detail;
}

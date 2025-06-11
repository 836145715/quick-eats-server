package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿添加/更新请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "地址簿添加/更新请求DTO")
public class AddressBookAddReqDTO implements Serializable {

    /**
     * 地址ID（更新时必传）
     */
    @Schema(description = "地址ID", example = "1")
    private Long id;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名", required = true, example = "张三")
    private String consignee;

    /**
     * 性别
     */
    @Schema(description = "性别：0女，1男", example = "1")
    private String sex;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号", required = true, example = "13800138000")
    private String phone;

    /**
     * 省份编码
     */
    @Schema(description = "省份编码", example = "110000")
    private String provinceCode;

    /**
     * 省份名称
     */
    @NotBlank(message = "省份不能为空")
    @Schema(description = "省份名称", required = true, example = "北京市")
    private String provinceName;

    /**
     * 城市编码
     */
    @Schema(description = "城市编码", example = "110100")
    private String cityCode;

    /**
     * 城市名称
     */
    @NotBlank(message = "城市不能为空")
    @Schema(description = "城市名称", required = true, example = "北京市")
    private String cityName;

    /**
     * 区县编码
     */
    @Schema(description = "区县编码", example = "110101")
    private String districtCode;

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

    /**
     * 地址标签
     */
    @Schema(description = "地址标签", example = "家")
    private String label;

    /**
     * 是否为默认地址
     */
    @Schema(description = "是否为默认地址", example = "true")
    private Boolean isDefault;
}

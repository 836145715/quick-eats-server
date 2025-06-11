package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "地址簿响应VO")
public class AddressBookVO implements Serializable {

    /**
     * 地址ID
     */
    @Schema(description = "地址ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 收货人姓名
     */
    @Schema(description = "收货人姓名", example = "张三")
    private String consignee;

    /**
     * 性别
     */
    @Schema(description = "性别：0女，1男", example = "1")
    private String sex;

    /**
     * 性别描述
     */
    @Schema(description = "性别描述", example = "男")
    private String sexText;

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 省份编码
     */
    @Schema(description = "省份编码", example = "110000")
    private String provinceCode;

    /**
     * 省份名称
     */
    @Schema(description = "省份名称", example = "北京市")
    private String provinceName;

    /**
     * 城市编码
     */
    @Schema(description = "城市编码", example = "110100")
    private String cityCode;

    /**
     * 城市名称
     */
    @Schema(description = "城市名称", example = "北京市")
    private String cityName;

    /**
     * 区县编码
     */
    @Schema(description = "区县编码", example = "110101")
    private String districtCode;

    /**
     * 区县名称
     */
    @Schema(description = "区县名称", example = "东城区")
    private String districtName;

    /**
     * 详细地址
     */
    @Schema(description = "详细地址", example = "王府井大街1号")
    private String detail;

    /**
     * 完整地址
     */
    @Schema(description = "完整地址", example = "北京市北京市东城区王府井大街1号")
    private String fullAddress;

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

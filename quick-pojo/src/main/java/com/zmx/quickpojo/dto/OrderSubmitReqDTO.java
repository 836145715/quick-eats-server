package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单提交请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单提交请求DTO")
public class OrderSubmitReqDTO implements Serializable {

    /**
     * 地址簿ID
     */
    @NotNull(message = "收货地址不能为空")
    @Schema(description = "地址簿ID", required = true, example = "1")
    private Long addressBookId;

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
    @Schema(description = "支付方式：1微信支付，2支付宝，3现金支付", required = true, example = "1")
    private Integer payMethod;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "少放辣椒")
    private String remark;

    /**
     * 预计送达时间
     */
    @Schema(description = "预计送达时间")
    private LocalDateTime estimatedDeliveryTime;

    /**
     * 餐具数量
     */
    @Schema(description = "餐具数量", example = "1")
    private Integer tablewareNumber;

    /**
     * 餐具数量状态
     */
    @Schema(description = "餐具数量状态：1按餐量提供，0选择具体数量", example = "1")
    private Integer tablewareStatus;
}

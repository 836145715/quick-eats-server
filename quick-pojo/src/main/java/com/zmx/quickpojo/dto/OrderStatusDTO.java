package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单状态DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单状态DTO")
public class OrderStatusDTO implements Serializable {

    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    @Schema(description = "订单ID", required = true)
    private Long id;

    /**
     * 订单状态
     */
    @NotNull(message = "订单状态不能为空")
    @Schema(description = "订单状态：1待付款，2待接单，3已接单，4派送中，5已完成，6已取消", required = true)
    private Integer status;

    /**
     * 拒绝原因（拒单时使用）
     */
    @Schema(description = "拒绝原因")
    private String rejectionReason;

    /**
     * 取消原因（取消订单时使用）
     */
    @Schema(description = "取消原因")
    private String cancelReason;
}

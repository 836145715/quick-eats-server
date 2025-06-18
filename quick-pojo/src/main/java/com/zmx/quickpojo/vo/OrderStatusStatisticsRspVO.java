package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 今日订单状态统计响应VO
 * 用于展示今日各个状态的订单数量统计
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "今日订单状态统计响应VO")
public class OrderStatusStatisticsRspVO {

    /**
     * 今日待接单订单数量
     * 对应订单状态：TO_BE_CONFIRMED(2)
     */
    @Schema(description = "今日待接单订单数量")
    private Integer toBeConfirmed;

    /**
     * 今日待派送订单数量
     * 对应订单状态：CONFIRMED(3)
     */
    @Schema(description = "今日待派送订单数量")
    private Integer confirmed;

    /**
     * 今日派送中订单数量
     * 对应订单状态：DELIVERY_IN_PROGRESS(4)
     */
    @Schema(description = "今日派送中订单数量")
    private Integer deliveryInProgress;

    /**
     * 今日已完成订单数量
     * 对应订单状态：COMPLETED(5)
     */
    @Schema(description = "今日已完成订单数量")
    private Integer completed;

    /**
     * 今日已取消订单数量
     * 对应订单状态：CANCELLED(6)
     */
    @Schema(description = "今日已取消订单数量")
    private Integer cancelled;

    /**
     * 今日全部订单数量
     * 今日所有状态订单的总数
     */
    @Schema(description = "今日全部订单数量")
    private Integer allOrders;
}

package com.zmx.quickpojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单分页查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单分页查询请求DTO")
public class OrderPageListReqDTO implements Serializable {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", defaultValue = "1")
    private Integer current = 1;

    /**
     * 每页记录数
     */
    @Schema(description = "每页记录数", defaultValue = "10")
    private Integer size = 10;

    /**
     * 订单号
     */
    @Schema(description = "订单号")
    private String number;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态：1待付款，2待接单，3已接单，4派送中，5已完成，6已取消")
    private Integer status;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /**
     * 用户ID（用户端查询时使用）
     */
    @Schema(description = "用户ID")
    private Long userId;
}

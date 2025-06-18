package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单待接单和待派送数量VO")
public class DashboardOrderCountRspVO {
    @Schema(description = "待接单数量")
    private Long waitAcceptCount;
    @Schema(description = "待派送数量")
    private Long waitDeliveryCount;
}

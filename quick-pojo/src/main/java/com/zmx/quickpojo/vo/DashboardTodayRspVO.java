package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "今日数据响应VO")
public class DashboardTodayRspVO {
    @Schema(description = "营业额")
    private Integer turnover;

    @Schema(description = "有效订单数量")
    private Integer orderValidNum;

    @Schema(description = "订单完成率")
    private Integer orderCompleteRate;

    @Schema(description = "平均客单价")
    private Integer averagePrice;

    @Schema(description = "新增用户数量")
    private Integer newUsers;
}

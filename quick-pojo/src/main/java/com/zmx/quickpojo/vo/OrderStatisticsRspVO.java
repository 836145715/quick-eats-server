package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单统计VO")
public class OrderStatisticsRspVO {
    @Schema(description = "日期列表")
    private List<String> dates = new ArrayList<>();

    @Schema(description = "订单总数列表")
    private List<Integer> totalNums = new ArrayList<>();

    @Schema(description = "有效订单列表")
    private List<Integer> validNums = new ArrayList<>();
}

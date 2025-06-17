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
@Schema(description = "营业额数据")
@NoArgsConstructor
@AllArgsConstructor
public class TurnoverStatisticsRspVO {
    @Schema(description = "日期列表")
    private List<String> dates = new ArrayList<>();

    @Schema(description = "营业额列表")
    private List<Double> turnovers = new ArrayList<>();
}

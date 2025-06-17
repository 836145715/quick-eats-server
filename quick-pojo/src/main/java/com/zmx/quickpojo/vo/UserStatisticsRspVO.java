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
@Schema(description = "用户统计响应VO")
public class UserStatisticsRspVO {
    @Schema(description = "日期列表")
    private List<String> dates = new ArrayList<>();

    @Schema(description = "新增用户列表")
    private List<Integer> newUsers = new ArrayList<>();

    @Schema(description = "用户总数列表")
    private List<Long> totalUsers = new ArrayList<>();
}

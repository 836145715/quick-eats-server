package com.zmx.quickserver.controller.admin;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.vo.DashboardDishSetMealVO;
import com.zmx.quickpojo.vo.DashboardOrderCountRspVO;
import com.zmx.quickpojo.vo.DashboardTodayRspVO;
import com.zmx.quickpojo.vo.OrderStatusStatisticsRspVO;
import com.zmx.quickserver.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/dashboard")
@Tag(name = "仪表盘接口", description = "仪表盘数据展示")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "获取今日数据", description = "获取今日数据接口")
    @GetMapping("/todayInfo")
    @ApiLog
    public Result<DashboardTodayRspVO> todayInfo() {
        log.info("获取今日数据统计");
        return Result.success(dashboardService.todayInfo());
    }

    /**
     * 获取今日订单状态统计
     * 统计今日各个状态的订单数量：待接单、待派送、派送中、已完成、已取消、全部订单
     *
     * @return 今日订单状态统计结果
     */
    @GetMapping("/orderStatusStatistics")
    @ApiLog
    @Operation(summary = "获取今日订单状态统计", description = "统计今日各个状态的订单数量")
    public Result<OrderStatusStatisticsRspVO> orderStatusStatistics() {
        log.info("获取今日订单状态统计");
        return Result.success(dashboardService.getOrderStatusStatistics());
    }

    @GetMapping("/dishSetMeal")
    @ApiLog
    @Operation(summary = "菜品和套餐数据", description = "菜品和套餐数据")
    public Result<DashboardDishSetMealVO> dishSetMeal() {
        log.info("菜品和套餐数据");
        return Result.success(dashboardService.getDishSetMeal());
    }

    @GetMapping("/orderCount")
    @ApiLog
    @Operation(summary = "订单数量统计", description = "订单数量统计")
    public Result<DashboardOrderCountRspVO> orderCount() {
        log.info("订单数量统计");
        return Result.success(dashboardService.getOrderCount());
    }

}

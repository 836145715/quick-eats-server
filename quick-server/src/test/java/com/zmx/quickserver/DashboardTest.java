package com.zmx.quickserver;

import com.zmx.quickpojo.vo.DashboardTodayRspVO;
import com.zmx.quickpojo.vo.OrderStatusStatisticsRspVO;
import com.zmx.quickserver.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 仪表盘功能测试类
 */
@SpringBootTest
@Slf4j
public class DashboardTest {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 测试今日数据统计
     */
    @Test
    public void testTodayInfo() {
        log.info("=== 开始测试今日数据统计 ===");
        
        DashboardTodayRspVO result = dashboardService.todayInfo();
        
        log.info("今日数据统计结果：");
        log.info("营业额：{} 元", result.getTurnover());
        log.info("有效订单数：{} 单", result.getOrderValidNum());
        log.info("订单完成率：{}%", result.getOrderCompleteRate());
        log.info("平均客单价：{} 元", result.getAveragePrice());
        log.info("新增用户数：{} 人", result.getNewUsers());
        
        // 验证数据合理性
        if (result.getTurnover() != null && result.getTurnover() >= 0) {
            log.info("✅ 营业额数据正常");
        } else {
            log.warn("⚠️ 营业额数据异常");
        }
        
        if (result.getOrderValidNum() != null && result.getOrderValidNum() >= 0) {
            log.info("✅ 有效订单数据正常");
        } else {
            log.warn("⚠️ 有效订单数据异常");
        }
        
        if (result.getOrderCompleteRate() != null && result.getOrderCompleteRate() >= 0 && result.getOrderCompleteRate() <= 100) {
            log.info("✅ 订单完成率数据正常");
        } else {
            log.warn("⚠️ 订单完成率数据异常");
        }
        
        log.info("=== 今日数据统计测试完成 ===");
    }

    /**
     * 测试今日订单状态统计
     */
    @Test
    public void testOrderStatusStatistics() {
        log.info("=== 开始测试今日订单状态统计 ===");

        OrderStatusStatisticsRspVO result = dashboardService.getOrderStatusStatistics();

        log.info("今日订单状态统计结果：");
        log.info("今日待接单：{} 单", result.getToBeConfirmed());
        log.info("今日待派送：{} 单", result.getConfirmed());
        log.info("今日派送中：{} 单", result.getDeliveryInProgress());
        log.info("今日已完成：{} 单", result.getCompleted());
        log.info("今日已取消：{} 单", result.getCancelled());
        log.info("今日全部订单：{} 单", result.getAllOrders());
        
        // 验证数据一致性
        int calculatedTotal = result.getToBeConfirmed() + result.getConfirmed() + 
                             result.getDeliveryInProgress() + result.getCompleted() + 
                             result.getCancelled();
        
        if (calculatedTotal == result.getAllOrders()) {
            log.info("✅ 今日订单状态统计数据一致性验证通过");
        } else {
            log.warn("⚠️ 今日订单状态统计数据不一致：计算总数={}, 实际总数={}",
                    calculatedTotal, result.getAllOrders());
        }

        // 验证各状态数据合理性
        if (result.getToBeConfirmed() >= 0 && result.getConfirmed() >= 0 &&
            result.getDeliveryInProgress() >= 0 && result.getCompleted() >= 0 &&
            result.getCancelled() >= 0 && result.getAllOrders() >= 0) {
            log.info("✅ 今日所有状态数据都为非负数");
        } else {
            log.warn("⚠️ 今日存在负数的状态数据");
        }

        log.info("=== 今日订单状态统计测试完成 ===");
    }

    /**
     * 测试数据完整性
     */
    @Test
    public void testDataIntegrity() {
        log.info("=== 开始测试数据完整性 ===");
        
        // 同时获取两个统计结果
        DashboardTodayRspVO todayInfo = dashboardService.todayInfo();
        OrderStatusStatisticsRspVO statusStats = dashboardService.getOrderStatusStatistics();
        
        log.info("数据完整性验证：");
        log.info("今日有效订单数：{}", todayInfo.getOrderValidNum());
        log.info("总已完成订单数：{}", statusStats.getCompleted());
        
        // 今日有效订单数应该小于等于总已完成订单数
        if (todayInfo.getOrderValidNum() <= statusStats.getCompleted()) {
            log.info("✅ 今日有效订单数与总已完成订单数关系正确");
        } else {
            log.warn("⚠️ 今日有效订单数大于总已完成订单数，可能存在数据问题");
        }
        
        log.info("=== 数据完整性测试完成 ===");
    }

    /**
     * 性能测试
     */
    @Test
    public void testPerformance() {
        log.info("=== 开始性能测试 ===");
        
        // 测试今日数据统计性能
        long startTime = System.currentTimeMillis();
        dashboardService.todayInfo();
        long todayInfoTime = System.currentTimeMillis() - startTime;
        log.info("今日数据统计耗时：{} ms", todayInfoTime);
        
        // 测试订单状态统计性能
        startTime = System.currentTimeMillis();
        dashboardService.getOrderStatusStatistics();
        long statusStatsTime = System.currentTimeMillis() - startTime;
        log.info("订单状态统计耗时：{} ms", statusStatsTime);
        
        // 性能基准：单次查询应在1秒内完成
        if (todayInfoTime < 1000 && statusStatsTime < 1000) {
            log.info("✅ 性能测试通过，查询响应时间在可接受范围内");
        } else {
            log.warn("⚠️ 性能测试警告，查询响应时间较长");
        }
        
        log.info("=== 性能测试完成 ===");
    }
}

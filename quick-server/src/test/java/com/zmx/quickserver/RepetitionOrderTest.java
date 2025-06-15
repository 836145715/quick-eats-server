package com.zmx.quickserver;

import com.zmx.common.response.Result;
import com.zmx.common.utils.BaseContext;
import com.zmx.quickpojo.entity.Orders;
import com.zmx.quickpojo.entity.OrderDetail;
import com.zmx.quickpojo.entity.ShoppingCart;
import com.zmx.quickserver.service.OrdersService;
import com.zmx.quickserver.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 再来一单功能测试类
 */
@SpringBootTest
@Slf4j
public class RepetitionOrderTest {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 测试再来一单功能
     */
    @Test
    @Transactional
    public void testRepetitionOrder() {
        log.info("=== 开始测试再来一单功能 ===");
        
        // 模拟用户登录（设置用户ID）
        Long testUserId = 1L;
        BaseContext.setCurrentId(testUserId);
        log.info("设置测试用户ID: {}", testUserId);
        
        // 查询用户的已完成订单
        List<Orders> userOrders = ordersService.lambdaQuery()
                .eq(Orders::getUserId, testUserId)
                .eq(Orders::getStatus, 5) // 已完成状态
                .orderByDesc(Orders::getOrderTime)
                .list();
        
        if (userOrders.isEmpty()) {
            log.warn("⚠️ 用户没有已完成的订单，无法测试再来一单功能");
            return;
        }
        
        Orders testOrder = userOrders.get(0);
        log.info("选择测试订单: ID={}, 订单号={}", testOrder.getId(), testOrder.getNumber());
        
        // 清空购物车（确保测试环境干净）
        Result<Void> cleanResult = shoppingCartService.clean();
        log.info("清空购物车结果: {}", cleanResult.getMsg());
        
        // 执行再来一单
        Result<Void> result = ordersService.repetitionOrder(testOrder.getId());
        
        if (result.getCode() == 200) {
            log.info("✅ 再来一单成功");
            
            // 验证购物车中是否有商品
            Result<List<com.zmx.quickpojo.vo.ShoppingCartVO>> cartResult = shoppingCartService.listCart();
            if (cartResult.getData() != null && !cartResult.getData().isEmpty()) {
                log.info("✅ 购物车验证成功，商品数量: {}", cartResult.getData().size());
                cartResult.getData().forEach(item -> 
                    log.info("购物车商品: {} x {}", item.getName(), item.getNumber())
                );
            } else {
                log.warn("⚠️ 购物车为空，可能存在问题");
            }
        } else {
            log.error("❌ 再来一单失败: {}", result.getMsg());
        }
        
        log.info("=== 再来一单功能测试完成 ===");
    }

    /**
     * 测试非法订单ID的再来一单
     */
    @Test
    public void testRepetitionOrderWithInvalidId() {
        log.info("=== 开始测试非法订单ID的再来一单 ===");
        
        // 模拟用户登录
        BaseContext.setCurrentId(1L);
        
        // 使用不存在的订单ID
        Long invalidOrderId = 99999L;
        Result<Void> result = ordersService.repetitionOrder(invalidOrderId);
        
        if (result.getCode() != 200) {
            log.info("✅ 正确处理了非法订单ID: {}", result.getMsg());
        } else {
            log.error("❌ 应该拒绝非法订单ID，但却成功了");
        }
        
        log.info("=== 非法订单ID测试完成 ===");
    }

    /**
     * 测试未登录用户的再来一单
     */
    @Test
    public void testRepetitionOrderWithoutLogin() {
        log.info("=== 开始测试未登录用户的再来一单 ===");
        
        // 清除用户登录状态
        BaseContext.setCurrentId(null);
        
        // 尝试再来一单
        Result<Void> result = ordersService.repetitionOrder(1L);
        
        if (result.getCode() != 200 && result.getMsg().contains("未登录")) {
            log.info("✅ 正确处理了未登录用户: {}", result.getMsg());
        } else {
            log.error("❌ 应该拒绝未登录用户，但却成功了");
        }
        
        log.info("=== 未登录用户测试完成 ===");
    }

    /**
     * 测试订单状态验证
     */
    @Test
    public void testRepetitionOrderStatusValidation() {
        log.info("=== 开始测试订单状态验证 ===");
        
        // 模拟用户登录
        BaseContext.setCurrentId(1L);
        
        // 查询非已完成状态的订单
        List<Orders> pendingOrders = ordersService.lambdaQuery()
                .eq(Orders::getUserId, 1L)
                .ne(Orders::getStatus, 5) // 非已完成状态
                .orderByDesc(Orders::getOrderTime)
                .list();
        
        if (!pendingOrders.isEmpty()) {
            Orders pendingOrder = pendingOrders.get(0);
            log.info("测试非已完成订单: ID={}, 状态={}", pendingOrder.getId(), pendingOrder.getStatus());
            
            Result<Void> result = ordersService.repetitionOrder(pendingOrder.getId());
            
            if (result.getCode() != 200 && result.getMsg().contains("已完成")) {
                log.info("✅ 正确验证了订单状态: {}", result.getMsg());
            } else {
                log.error("❌ 应该拒绝非已完成订单，但却成功了");
            }
        } else {
            log.warn("⚠️ 没有找到非已完成状态的订单进行测试");
        }
        
        log.info("=== 订单状态验证测试完成 ===");
    }
}

package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zmx.common.constants.OrderConstant;
import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickpojo.entity.Orders;
import com.zmx.quickpojo.entity.Setmeal;
import com.zmx.quickpojo.entity.User;
import com.zmx.quickpojo.vo.DashboardDishSetMealVO;
import com.zmx.quickpojo.vo.DashboardOrderCountRspVO;
import com.zmx.quickpojo.vo.DashboardTodayRspVO;
import com.zmx.quickpojo.vo.OrderStatusStatisticsRspVO;
import com.zmx.quickserver.mapper.DishMapper;
import com.zmx.quickserver.mapper.OrdersMapper;
import com.zmx.quickserver.mapper.SetmealMapper;
import com.zmx.quickserver.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * 仪表盘服务类
 * 提供今日数据统计功能
 */
@Service
@Slf4j
public class DashboardService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 获取今日数据统计
     * 包括营业额、有效订单数、订单完成率、平均客单价、新增用户数
     *
     * @return 今日数据统计结果
     */
    public DashboardTodayRspVO todayInfo() {
        log.info("获取今日数据统计");

        // 获取今日开始和结束时间
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        try {
            // 1. 统计今日营业额（已完成订单的总金额）
            Integer turnover = getTodayTurnover(todayStart, todayEnd);

            // 2. 统计今日有效订单数量（已完成订单数量）
            Integer orderValidNum = getTodayValidOrderCount(todayStart, todayEnd);

            // 3. 计算今日订单完成率（有效订单数 / 总订单数 * 100）
            Integer orderCompleteRate = getTodayOrderCompleteRate(todayStart, todayEnd);

            // 4. 计算今日平均客单价（营业额 / 有效订单数）
            Integer averagePrice = getTodayAveragePrice(turnover, orderValidNum);

            // 5. 统计今日新增用户数量
            Integer newUsers = getTodayNewUserCount(todayStart, todayEnd);

            // 构建返回结果
            DashboardTodayRspVO result = DashboardTodayRspVO.builder()
                    .turnover(turnover)
                    .orderValidNum(orderValidNum)
                    .orderCompleteRate(orderCompleteRate)
                    .averagePrice(averagePrice)
                    .newUsers(newUsers)
                    .build();

            log.info("今日数据统计完成：营业额={}，有效订单={}，完成率={}%，客单价={}，新用户={}",
                    turnover, orderValidNum, orderCompleteRate, averagePrice, newUsers);

            return result;

        } catch (Exception e) {
            log.error("获取今日数据统计失败", e);
            // 返回默认值，避免前端报错
            return DashboardTodayRspVO.builder()
                    .turnover(0)
                    .orderValidNum(0)
                    .orderCompleteRate(0)
                    .averagePrice(0)
                    .newUsers(0)
                    .build();
        }
    }

    /**
     * 获取今日营业额
     * 统计已完成订单的总金额
     */
    private Integer getTodayTurnover(LocalDateTime todayStart, LocalDateTime todayEnd) {
        try {
            List<Map<String, Object>> turnoverData = ordersMapper.turnoverStatistics(
                    OrderConstant.Status.COMPLETED, todayStart, todayEnd);

            if (turnoverData == null || turnoverData.isEmpty()) {
                return 0;
            }

            // 获取今日营业额
            Object amountObj = turnoverData.get(0).get("amount");
            if (amountObj == null) {
                return 0;
            }

            BigDecimal amount = new BigDecimal(amountObj.toString());
            return amount.intValue();

        } catch (Exception e) {
            log.error("获取今日营业额失败", e);
            return 0;
        }
    }

    /**
     * 获取今日有效订单数量
     * 统计已完成状态的订单数量
     */
    private Integer getTodayValidOrderCount(LocalDateTime todayStart, LocalDateTime todayEnd) {
        try {
            LambdaQueryWrapper<Orders> query = new LambdaQueryWrapper<>();
            query.eq(Orders::getStatus, OrderConstant.Status.COMPLETED)
                 .ge(Orders::getOrderTime, todayStart)
                 .le(Orders::getOrderTime, todayEnd);

            return Math.toIntExact(ordersMapper.selectCount(query));

        } catch (Exception e) {
            log.error("获取今日有效订单数量失败", e);
            return 0;
        }
    }

    /**
     * 计算今日订单完成率
     * 完成率 = 有效订单数 / 总订单数 * 100
     */
    private Integer getTodayOrderCompleteRate(LocalDateTime todayStart, LocalDateTime todayEnd) {
        try {
            // 统计今日总订单数
            LambdaQueryWrapper<Orders> totalQuery = new LambdaQueryWrapper<>();
            totalQuery.ge(Orders::getOrderTime, todayStart)
                     .le(Orders::getOrderTime, todayEnd);
            long totalOrders = ordersMapper.selectCount(totalQuery);

            if (totalOrders == 0) {
                return 0;
            }

            // 统计今日有效订单数
            LambdaQueryWrapper<Orders> validQuery = new LambdaQueryWrapper<>();
            validQuery.eq(Orders::getStatus, OrderConstant.Status.COMPLETED)
                     .ge(Orders::getOrderTime, todayStart)
                     .le(Orders::getOrderTime, todayEnd);
            long validOrders = ordersMapper.selectCount(validQuery);

            // 计算完成率（百分比）
            BigDecimal rate = BigDecimal.valueOf(validOrders)
                    .divide(BigDecimal.valueOf(totalOrders), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            return rate.intValue();

        } catch (Exception e) {
            log.error("计算今日订单完成率失败", e);
            return 0;
        }
    }

    /**
     * 计算今日平均客单价
     * 平均客单价 = 营业额 / 有效订单数
     */
    private Integer getTodayAveragePrice(Integer turnover, Integer orderValidNum) {
        try {
            if (orderValidNum == 0 || turnover == 0) {
                return 0;
            }

            BigDecimal avgPrice = BigDecimal.valueOf(turnover)
                    .divide(BigDecimal.valueOf(orderValidNum), 2, RoundingMode.HALF_UP);

            return avgPrice.intValue();

        } catch (Exception e) {
            log.error("计算今日平均客单价失败", e);
            return 0;
        }
    }

    /**
     * 获取今日新增用户数量
     */
    private Integer getTodayNewUserCount(LocalDateTime todayStart, LocalDateTime todayEnd) {
        try {
            LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
            query.ge(User::getCreateTime, todayStart)
                 .le(User::getCreateTime, todayEnd);

            return Math.toIntExact(userMapper.selectCount(query));

        } catch (Exception e) {
            log.error("获取今日新增用户数量失败", e);
            return 0;
        }
    }

    /**
     * 获取今日订单状态统计
     * 统计今日各个状态的订单数量
     *
     * @return 今日订单状态统计结果
     */
    public OrderStatusStatisticsRspVO getOrderStatusStatistics() {
        log.info("获取今日订单状态统计");

        // 获取今日开始和结束时间
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        try {
            // 统计今日待接单订单数量
            Integer toBeConfirmed = getTodayOrderCountByStatus(OrderConstant.Status.TO_BE_CONFIRMED, todayStart, todayEnd);

            // 统计今日待派送订单数量（已接单）
            Integer confirmed = getTodayOrderCountByStatus(OrderConstant.Status.CONFIRMED, todayStart, todayEnd);

            // 统计今日派送中订单数量
            Integer deliveryInProgress = getTodayOrderCountByStatus(OrderConstant.Status.DELIVERY_IN_PROGRESS, todayStart, todayEnd);

            // 统计今日已完成订单数量
            Integer completed = getTodayOrderCountByStatus(OrderConstant.Status.COMPLETED, todayStart, todayEnd);

            // 统计今日已取消订单数量
            Integer cancelled = getTodayOrderCountByStatus(OrderConstant.Status.CANCELLED, todayStart, todayEnd);

            // 统计今日全部订单数量
            Integer allOrders = getTodayAllOrderCount(todayStart, todayEnd);

            // 构建返回结果
            OrderStatusStatisticsRspVO result = OrderStatusStatisticsRspVO.builder()
                    .toBeConfirmed(toBeConfirmed)
                    .confirmed(confirmed)
                    .deliveryInProgress(deliveryInProgress)
                    .completed(completed)
                    .cancelled(cancelled)
                    .allOrders(allOrders)
                    .build();

            log.info("今日订单状态统计完成：待接单={}，待派送={}，派送中={}，已完成={}，已取消={}，全部={}",
                    toBeConfirmed, confirmed, deliveryInProgress, completed, cancelled, allOrders);

            return result;

        } catch (Exception e) {
            log.error("获取今日订单状态统计失败", e);
            // 返回默认值，避免前端报错
            return OrderStatusStatisticsRspVO.builder()
                    .toBeConfirmed(0)
                    .confirmed(0)
                    .deliveryInProgress(0)
                    .completed(0)
                    .cancelled(0)
                    .allOrders(0)
                    .build();
        }
    }

    /**
     * 根据订单状态统计今日订单数量
     *
     * @param status 订单状态
     * @param todayStart 今日开始时间
     * @param todayEnd 今日结束时间
     * @return 今日订单数量
     */
    private Integer getTodayOrderCountByStatus(Integer status, LocalDateTime todayStart, LocalDateTime todayEnd) {
        try {
            LambdaQueryWrapper<Orders> query = new LambdaQueryWrapper<>();
            query.eq(Orders::getStatus, status)
                 .ge(Orders::getOrderTime, todayStart)
                 .le(Orders::getOrderTime, todayEnd);

            return Math.toIntExact(ordersMapper.selectCount(query));

        } catch (Exception e) {
            log.error("统计今日状态为{}的订单数量失败", status, e);
            return 0;
        }
    }

    /**
     * 统计今日全部订单数量
     *
     * @param todayStart 今日开始时间
     * @param todayEnd 今日结束时间
     * @return 今日全部订单数量
     */
    private Integer getTodayAllOrderCount(LocalDateTime todayStart, LocalDateTime todayEnd) {
        try {
            LambdaQueryWrapper<Orders> query = new LambdaQueryWrapper<>();
            query.ge(Orders::getOrderTime, todayStart)
                 .le(Orders::getOrderTime, todayEnd);

            return Math.toIntExact(ordersMapper.selectCount(query));

        } catch (Exception e) {
            log.error("统计今日全部订单数量失败", e);
            return 0;
        }
    }

    public DashboardDishSetMealVO getDishSetMeal() {
        DashboardDishSetMealVO result = new DashboardDishSetMealVO();
        LambdaQueryWrapper<Dish> dishQuery = new LambdaQueryWrapper<>();
        dishQuery.eq(Dish::getStatus, 1);
        result.setDishEnableCount(dishMapper.selectCount(dishQuery));

        LambdaQueryWrapper<Dish> dishQuery2 = new LambdaQueryWrapper<>();
        dishQuery2.eq(Dish::getStatus, 0);
        result.setDishDisableCount(dishMapper.selectCount(dishQuery2));

        LambdaQueryWrapper<Setmeal> setmealQuery = new LambdaQueryWrapper<>();
        setmealQuery.eq(Setmeal::getStatus, 1);
        result.setSetMealEnableCount(setmealMapper.selectCount(setmealQuery));

        LambdaQueryWrapper<Setmeal> setmealQuery2 = new LambdaQueryWrapper<>();
        setmealQuery2.eq(Setmeal::getStatus, 0);
        result.setSetMealDisableCount(setmealMapper.selectCount(setmealQuery2));
        return result;
    }

    public DashboardOrderCountRspVO getOrderCount() {
        var result = new DashboardOrderCountRspVO();
        LambdaQueryWrapper<Orders> query = new LambdaQueryWrapper<>();
        query.eq(Orders::getStatus, OrderConstant.Status.TO_BE_CONFIRMED);
        result.setWaitAcceptCount(ordersMapper.selectCount(query));

        query.clear();
        query.eq(Orders::getStatus, OrderConstant.Status.CONFIRMED);
                result.setWaitDeliveryCount(ordersMapper.selectCount(query));
                return result;
    }
}

package com.zmx.common.utils;

import com.zmx.common.constants.OrderConstant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单工具类
 */
public class OrderUtils {

    private static final AtomicLong ORDER_SEQUENCE = new AtomicLong(1);

    /**
     * 生成订单号
     * 格式：QE + yyyyMMddHHmmss + 4位序列号
     *
     * @return 订单号
     */
    public static String generateOrderNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long sequence = ORDER_SEQUENCE.getAndIncrement();
        if (sequence > 9999) {
            ORDER_SEQUENCE.set(1);
            sequence = 1;
        }
        return OrderConstant.ORDER_NUMBER_PREFIX + timestamp + String.format("%04d", sequence);
    }

    /**
     * 获取订单状态描述
     *
     * @param status 订单状态
     * @return 状态描述
     */
    public static String getOrderStatusText(Integer status) {
        if (status == null) {
            return "未知状态";
        }
        
        switch (status) {
            case 1:
                return "待付款";
            case 2:
                return "待接单";
            case 3:
                return "已接单";
            case 4:
                return "派送中";
            case 5:
                return "已完成";
            case 6:
                return "已取消";
            default:
                return "未知状态";
        }
    }

    /**
     * 获取支付方式描述
     *
     * @param payMethod 支付方式
     * @return 支付方式描述
     */
    public static String getPayMethodText(Integer payMethod) {
        if (payMethod == null) {
            return "未知支付方式";
        }
        
        switch (payMethod) {
            case 1:
                return "微信支付";
            case 2:
                return "支付宝";
            case 3:
                return "现金支付";
            default:
                return "未知支付方式";
        }
    }

    /**
     * 获取支付状态描述
     *
     * @param payStatus 支付状态
     * @return 支付状态描述
     */
    public static String getPayStatusText(Integer payStatus) {
        if (payStatus == null) {
            return "未知支付状态";
        }
        
        switch (payStatus) {
            case 0:
                return "未支付";
            case 1:
                return "已支付";
            case 2:
                return "退款";
            default:
                return "未知支付状态";
        }
    }

    /**
     * 获取配送状态描述
     *
     * @param deliveryStatus 配送状态
     * @return 配送状态描述
     */
    public static String getDeliveryStatusText(Integer deliveryStatus) {
        if (deliveryStatus == null) {
            return "未知配送状态";
        }
        
        switch (deliveryStatus) {
            case 0:
                return "未配送";
            case 1:
                return "配送中";
            case 2:
                return "已送达";
            default:
                return "未知配送状态";
        }
    }

    /**
     * 判断订单是否可以取消
     *
     * @param status 订单状态
     * @return 是否可以取消
     */
    public static boolean canCancel(Integer status) {
        return status != null && (status.equals(OrderConstant.Status.PENDING_PAYMENT) 
                || status.equals(OrderConstant.Status.TO_BE_CONFIRMED));
    }

    /**
     * 判断订单是否可以接单
     *
     * @param status 订单状态
     * @return 是否可以接单
     */
    public static boolean canConfirm(Integer status) {
        return status != null && status.equals(OrderConstant.Status.TO_BE_CONFIRMED);
    }

    /**
     * 判断订单是否可以拒单
     *
     * @param status 订单状态
     * @return 是否可以拒单
     */
    public static boolean canReject(Integer status) {
        return status != null && status.equals(OrderConstant.Status.TO_BE_CONFIRMED);
    }

    /**
     * 判断订单是否可以派送
     *
     * @param status 订单状态
     * @return 是否可以派送
     */
    public static boolean canDeliver(Integer status) {
        return status != null && status.equals(OrderConstant.Status.CONFIRMED);
    }

    /**
     * 判断订单是否可以完成
     *
     * @param status 订单状态
     * @return 是否可以完成
     */
    public static boolean canComplete(Integer status) {
        return status != null && status.equals(OrderConstant.Status.DELIVERY_IN_PROGRESS);
    }
}

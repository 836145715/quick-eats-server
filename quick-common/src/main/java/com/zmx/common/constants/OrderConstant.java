package com.zmx.common.constants;

/**
 * 订单相关常量
 */
public class OrderConstant {

    /**
     * 订单状态常量类
     * 定义订单在整个生命周期中的各种状态
     * 状态流转：待付款 → 待接单 → 已接单 → 派送中 → 已完成
     * 任何状态都可能转为：已取消
     */
    public static class Status {
        /**
         * 待付款状态 - 订单已创建，等待用户支付
         * 用户可以取消订单
         */
        public static final Integer PENDING_PAYMENT = 1;

        /**
         * 待接单状态 - 用户已支付，等待商家确认接单
         * 商家可以接单或拒单，用户可以取消订单
         */
        public static final Integer TO_BE_CONFIRMED = 2;

        /**
         * 已接单状态 - 商家已确认接单，正在准备商品
         * 商家可以开始配送，用户无法取消订单
         */
        public static final Integer CONFIRMED = 3;

        /**
         * 派送中状态 - 订单正在配送途中
         * 等待送达用户，用户可以催单
         */
        public static final Integer DELIVERY_IN_PROGRESS = 4;

        /**
         * 已完成状态 - 订单已送达并完成
         * 订单流程结束，用户可以进行"再来一单"操作
         */
        public static final Integer COMPLETED = 5;

        /**
         * 已取消状态 - 订单被取消
         * 可能由用户取消、商家拒单或系统自动取消
         */
        public static final Integer CANCELLED = 6;
    }

    /**
     * 支付状态
     */
    public static class PayStatus {
        /** 未支付 */
        public static final Integer UNPAID = 0;
        /** 已支付 */
        public static final Integer PAID = 1;
        /** 退款 */
        public static final Integer REFUND = 2;
    }

    /**
     * 支付方式
     */
    public static class PayMethod {
        /** 微信支付 */
        public static final Integer WECHAT = 1;
        /** 支付宝 */
        public static final Integer ALIPAY = 2;
        /** 现金支付 */
        public static final Integer CASH = 3;
    }

    /**
     * 配送状态
     */
    public static class DeliveryStatus {
        /** 未配送 */
        public static final Integer NOT_DELIVERED = 0;
        /** 配送中 */
        public static final Integer DELIVERING = 1;
        /** 已送达 */
        public static final Integer DELIVERED = 2;
    }

    /**
     * 订单号前缀
     */
    public static final String ORDER_NUMBER_PREFIX = "QE";

    /**
     * 默认配送费
     */
    public static final Integer DEFAULT_PACK_AMOUNT = 6;

    /**
     * 订单超时时间（分钟）
     */
    public static final Integer ORDER_TIMEOUT_MINUTES = 15;
}

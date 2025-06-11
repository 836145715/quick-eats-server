package com.zmx.quickpojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单分页查询响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单分页查询响应VO")
public class OrderPageListRspVO implements Serializable {

    /**
     * 订单ID
     */
    @Schema(description = "订单ID", example = "1")
    private Long id;

    /**
     * 订单号
     */
    @Schema(description = "订单号", example = "QE202312010001")
    private String number;

    /**
     * 订单状态
     */
    @Schema(description = "订单状态：1待付款，2待接单，3已接单，4派送中，5已完成，6已取消", example = "2")
    private Integer status;

    /**
     * 订单状态描述
     */
    @Schema(description = "订单状态描述", example = "待接单")
    private String statusText;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "张三")
    private String userName;

    /**
     * 手机号
     */
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    /**
     * 收货地址
     */
    @Schema(description = "收货地址", example = "北京市朝阳区xxx街道xxx号")
    private String address;

    /**
     * 收货人
     */
    @Schema(description = "收货人", example = "张三")
    private String consignee;

    /**
     * 下单时间
     */
    @Schema(description = "下单时间")
    private LocalDateTime orderTime;

    /**
     * 结账时间
     */
    @Schema(description = "结账时间")
    private LocalDateTime checkoutTime;

    /**
     * 支付方式
     */
    @Schema(description = "支付方式：1微信支付，2支付宝，3现金支付", example = "1")
    private Integer payMethod;

    /**
     * 支付方式描述
     */
    @Schema(description = "支付方式描述", example = "微信支付")
    private String payMethodText;

    /**
     * 支付状态
     */
    @Schema(description = "支付状态：0未支付，1已支付，2退款", example = "1")
    private Integer payStatus;

    /**
     * 订单金额
     */
    @Schema(description = "订单金额", example = "68.00")
    private BigDecimal amount;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "少放辣椒")
    private String remark;

    /**
     * 预计送达时间
     */
    @Schema(description = "预计送达时间")
    private LocalDateTime estimatedDeliveryTime;

    /**
     * 配送状态
     */
    @Schema(description = "配送状态：0未配送，1配送中，2已送达", example = "0")
    private Integer deliveryStatus;

    /**
     * 配送时间
     */
    @Schema(description = "配送时间")
    private LocalDateTime deliveryTime;

    /**
     * 打包费
     */
    @Schema(description = "打包费", example = "6")
    private Integer packAmount;

    /**
     * 订单菜品详情
     */
    @Schema(description = "订单菜品详情")
    private String orderDishes;

    /**
     * 订单明细列表
     */
    @Schema(description = "订单明细列表")
    private List<OrderDetailVO> orderDetailList;
}

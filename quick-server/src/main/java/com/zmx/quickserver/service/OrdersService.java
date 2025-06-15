package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.OrderPageListReqDTO;
import com.zmx.quickpojo.dto.OrderStatusDTO;
import com.zmx.quickpojo.dto.OrderSubmitReqDTO;
import com.zmx.quickpojo.entity.Orders;
import com.zmx.quickpojo.vo.OrderPageListRspVO;
import com.zmx.quickpojo.vo.OrderSubmitRspVO;

/**
 * 订单Service接口
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param orderSubmitDTO 订单提交信息
     * @return 订单提交结果
     */
    Result<OrderSubmitRspVO> submitOrder(OrderSubmitReqDTO orderSubmitDTO);

    /**
     * 订单分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    PageResult<OrderPageListRspVO> pageList(OrderPageListReqDTO dto);

    /**
     * 根据ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    Result<OrderPageListRspVO> getById(Long id);

    /**
     * 取消订单
     *
     * @param id           订单ID
     * @param cancelReason 取消原因
     * @return 操作结果
     */
    Result<Void> cancelOrder(Long id, String cancelReason);

    /**
     * 接单
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    Result<Void> confirmOrder(OrderStatusDTO statusDTO);

    /**
     * 拒单
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    Result<Void> rejectOrder(OrderStatusDTO statusDTO);

    /**
     * 派送订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    Result<Void> deliverOrder(Long id);

    /**
     * 完成订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    Result<Void> completeOrder(Long id);

    /**
     * 催单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    Result<Void> reminderOrder(Long id);

    /**
     * 再来一单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    Result<Void> repetitionOrder(Long id);
}
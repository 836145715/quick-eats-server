package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.constants.OrderConstant;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.common.utils.BaseContext;
import com.zmx.common.utils.OrderUtils;
import com.zmx.quickpojo.dto.OrderPageListReqDTO;
import com.zmx.quickpojo.dto.OrderStatusDTO;
import com.zmx.quickpojo.dto.OrderSubmitReqDTO;
import com.zmx.quickpojo.entity.*;
import com.zmx.quickpojo.vo.OrderDetailVO;
import com.zmx.quickpojo.vo.OrderPageListRspVO;
import com.zmx.quickpojo.vo.OrderSubmitRspVO;
import com.zmx.quickserver.config.WebSocketServer;
import com.zmx.quickserver.mapper.OrdersMapper;
import com.zmx.quickserver.service.*;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单Service实现类
 */
@Service
@Slf4j
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户下单
     *
     * @param orderSubmitDTO 订单提交信息
     * @return 订单提交结果
     */
    @Override
    @Transactional
    public Result<OrderSubmitRspVO> submitOrder(OrderSubmitReqDTO orderSubmitDTO) {
        log.info("用户下单：{}", orderSubmitDTO);

        // 1. 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 2. 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> cartQuery = new LambdaQueryWrapper<>();
        cartQuery.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(cartQuery);

        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            return Result.error("购物车为空，不能下单");
        }

        // 3. 查询用户地址数据
        AddressBook addressBook = addressBookService.getById(orderSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            return Result.error("收货地址信息有误，不能下单");
        }

        // 4. 向订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(OrderConstant.Status.PENDING_PAYMENT);
        orders.setAmount(getOrderAmount(shoppingCartList));
        orders.setNumber(OrderUtils.generateOrderNumber());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName()
                + addressBook.getDetail());
        orders.setPayStatus(OrderConstant.PayStatus.UNPAID);
        orders.setPackAmount(OrderConstant.DEFAULT_PACK_AMOUNT);

        save(orders);

        // 5. 向订单明细表插入多条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderDetailService.saveBatch(orderDetailList);

        // 6. 清空当前用户的购物车数据
        shoppingCartService.remove(cartQuery);

        // 7. 封装返回结果
        OrderSubmitRspVO orderSubmitVO = OrderSubmitRspVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        return Result.success(orderSubmitVO);
    }

    /**
     * 计算订单金额
     *
     * @param shoppingCartList 购物车列表
     * @return 订单金额
     */
    private BigDecimal getOrderAmount(List<ShoppingCart> shoppingCartList) {
        BigDecimal amount = BigDecimal.ZERO;
        for (ShoppingCart cart : shoppingCartList) {
            amount = amount.add(cart.getAmount());
        }
        return amount;
    }

    /**
     * 订单分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    @Override
    public PageResult<OrderPageListRspVO> pageList(OrderPageListReqDTO dto) {
        log.info("订单分页查询：{}", dto);

        // 构建查询条件
        LambdaQueryChainWrapper<Orders> query = lambdaQuery();

        // 用户端查询时过滤用户ID
        if (dto.getUserId() != null) {
            query.eq(Orders::getUserId, dto.getUserId());
        }

        // 订单号模糊查询
        if (dto.getNumber() != null && !dto.getNumber().trim().isEmpty()) {
            query.like(Orders::getNumber, dto.getNumber().trim());
        }

        // 手机号模糊查询
        if (dto.getPhone() != null && !dto.getPhone().trim().isEmpty()) {
            query.like(Orders::getPhone, dto.getPhone().trim());
        }

        // 订单状态查询
        if (dto.getStatus() != null) {
            query.eq(Orders::getStatus, dto.getStatus());
        }

        // 时间范围查询
        if (dto.getBeginTime() != null) {
            query.ge(Orders::getOrderTime, dto.getBeginTime());
        }
        if (dto.getEndTime() != null) {
            query.le(Orders::getOrderTime, dto.getEndTime());
        }

        // 按下单时间倒序排列
        query.orderByDesc(Orders::getOrderTime);

        // 分页查询
        Page<Orders> page = new Page<>(dto.getCurrent(), dto.getSize());
        IPage<Orders> pageResult = query.page(page);

        // 转换为VO
        List<Orders> records = pageResult.getRecords();
        List<OrderPageListRspVO> vos = new ArrayList<>();

        for (Orders order : records) {
            OrderPageListRspVO vo = new OrderPageListRspVO();
            BeanUtils.copyProperties(order, vo);

            // 设置状态描述
            vo.setStatusText(OrderUtils.getOrderStatusText(order.getStatus()));
            vo.setPayMethodText(OrderUtils.getPayMethodText(order.getPayMethod()));

            // 查询订单明细
            LambdaQueryWrapper<OrderDetail> detailQuery = new LambdaQueryWrapper<>();
            detailQuery.eq(OrderDetail::getOrderId, order.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(detailQuery);

            // 转换订单明细
            List<OrderDetailVO> detailVOs = orderDetails.stream().map(detail -> {
                OrderDetailVO detailVO = new OrderDetailVO();
                BeanUtils.copyProperties(detail, detailVO);
                return detailVO;
            }).collect(Collectors.toList());

            vo.setOrderDetailList(detailVOs);

            // 生成订单菜品描述
            String orderDishes = orderDetails.stream()
                    .map(detail -> detail.getName() + "*" + detail.getNumber())
                    .collect(Collectors.joining("；"));
            vo.setOrderDishes(orderDishes);

            vos.add(vo);
        }

        return PageResult.success(pageResult, vos);
    }

    /**
     * 根据ID查询订单详情
     *
     * @param id 订单ID
     * @return 订单详情
     */
    @Override
    public Result<OrderPageListRspVO> getById(Long id) {
        log.info("查询订单详情：{}", id);

        Orders order = super.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        OrderPageListRspVO vo = new OrderPageListRspVO();
        BeanUtils.copyProperties(order, vo);

        // 设置状态描述
        vo.setStatusText(OrderUtils.getOrderStatusText(order.getStatus()));
        vo.setPayMethodText(OrderUtils.getPayMethodText(order.getPayMethod()));

        // 查询订单明细
        LambdaQueryWrapper<OrderDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(OrderDetail::getOrderId, order.getId());
        List<OrderDetail> orderDetails = orderDetailService.list(detailQuery);

        // 转换订单明细
        List<OrderDetailVO> detailVOs = orderDetails.stream().map(detail -> {
            OrderDetailVO detailVO = new OrderDetailVO();
            BeanUtils.copyProperties(detail, detailVO);
            return detailVO;
        }).collect(Collectors.toList());

        vo.setOrderDetailList(detailVOs);

        // 生成订单菜品描述
        String orderDishes = orderDetails.stream()
                .map(detail -> detail.getName() + "*" + detail.getNumber())
                .collect(Collectors.joining("；"));
        vo.setOrderDishes(orderDishes);

        return Result.success(vo);
    }

    /**
     * 取消订单
     *
     * @param id           订单ID
     * @param cancelReason 取消原因
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> cancelOrder(Long id, String cancelReason) {
        log.info("取消订单：{}", id);

        Orders order = super.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!OrderUtils.canCancel(order.getStatus())) {
            return Result.error("订单状态不允许取消");
        }

        // 更新订单状态
        Orders updateOrder = new Orders();
        updateOrder.setId(id);
        updateOrder.setStatus(OrderConstant.Status.CANCELLED);
        updateOrder.setCancelReason(cancelReason);
        updateOrder.setCancelTime(LocalDateTime.now());

        updateById(updateOrder);

        return Result.success();
    }

    /**
     * 接单
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> confirmOrder(Long orderId) {
        log.info("接单：{}", orderId);

        Orders order = super.getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!OrderUtils.canConfirm(order.getStatus())) {
            return Result.error("订单状态不允许接单");
        }

        // 更新订单状态
        Orders updateOrder = new Orders();
        updateOrder.setId(orderId);
        updateOrder.setStatus(OrderConstant.Status.CONFIRMED);
        updateById(updateOrder);
        return Result.success();
    }

    /**
     * 拒单
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> rejectOrder(Long orderId) {
        log.info("拒单：{}", orderId);

        Orders order = super.getById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!OrderUtils.canReject(order.getStatus())) {
            return Result.error("订单状态不允许拒单");
        }

        // 更新订单状态
        Orders updateOrder = new Orders();
        updateOrder.setId(orderId);
        updateOrder.setStatus(OrderConstant.Status.CANCELLED);
        updateOrder.setRejectionReason("商家拒单");
        updateOrder.setCancelTime(LocalDateTime.now());

        updateById(updateOrder);

        return Result.success();
    }

    /**
     * 派送订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> deliverOrder(Long id) {
        log.info("派送订单：{}", id);

        Orders order = super.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!OrderUtils.canDeliver(order.getStatus())) {
            return Result.error("订单状态不允许派送");
        }

        // 更新订单状态
        Orders updateOrder = new Orders();
        updateOrder.setId(id);
        updateOrder.setStatus(OrderConstant.Status.DELIVERY_IN_PROGRESS);
        updateOrder.setDeliveryTime(LocalDateTime.now());

        updateById(updateOrder);

        return Result.success();
    }

    /**
     * 完成订单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> completeOrder(Long id) {
        log.info("完成订单：{}", id);

        Orders order = super.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (!OrderUtils.canComplete(order.getStatus())) {
            return Result.error("订单状态不允许完成");
        }

        // 更新订单状态
        Orders updateOrder = new Orders();
        updateOrder.setId(id);
        updateOrder.setStatus(OrderConstant.Status.COMPLETED);
        updateOrder.setDeliveryTime(LocalDateTime.now());

        updateById(updateOrder);

        return Result.success();
    }

    /**
     * 催单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @Override
    public Result<Void> reminderOrder(Long id) {
        log.info("催单：{}", id);

        Orders order = super.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        // 这里可以发送催单通知给商家
        // 比如发送WebSocket消息、短信通知等
        log.info("向商家发送催单通知，订单号：{}", order.getNumber());
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2); // 1: 来单提醒 2. 催单提醒
        map.put("orderNumber", order.getNumber());
        // 转为json字符串
        String json = JSONUtil.toJsonStr(map);
        webSocketServer.sendAllMessage(json);

        return Result.success();
    }

    /**
     * 再来一单
     *
     * @param id 订单ID
     * @return 操作结果
     */
    @Override
    @Transactional
    public Result<Void> repetitionOrder(Long id) {
        log.info("再来一单：{}", id);

        // 1. 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        // 2. 查询订单是否存在
        Orders order = super.getById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        // 3. 验证订单是否属于当前用户
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权限操作此订单");
        }

        // 4. 验证订单状态是否允许再来一单
        if (!OrderUtils.canRepetition(order.getStatus())) {
            return Result.error("只有已完成的订单才能再来一单");
        }

        // 5. 查询订单详情
        LambdaQueryWrapper<OrderDetail> detailQuery = new LambdaQueryWrapper<>();
        detailQuery.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> orderDetails = orderDetailService.list(detailQuery);

        if (orderDetails == null || orderDetails.isEmpty()) {
            return Result.error("订单详情不存在");
        }

        // 6. 验证商品是否还在售
        for (OrderDetail detail : orderDetails) {
            if (detail.getDishId() != null) {
                // 验证菜品是否存在且在售
                Dish dish = dishService.getById(detail.getDishId());
                if (dish == null || dish.getStatus() == 0) {
                    return Result.error("商品【" + detail.getName() + "】已下架，无法再来一单");
                }
            } else if (detail.getSetmealId() != null) {
                // 验证套餐是否存在且在售
                Setmeal setmeal = setmealService.getById(detail.getSetmealId());
                if (setmeal == null || setmeal.getStatus() == 0) {
                    return Result.error("商品【" + detail.getName() + "】已下架，无法再来一单");
                }
            }
        }

        // 7. 将订单商品重新加入购物车
        for (OrderDetail detail : orderDetails) {
            // 检查购物车中是否已存在相同商品
            LambdaQueryWrapper<ShoppingCart> cartQuery = new LambdaQueryWrapper<>();
            cartQuery.eq(ShoppingCart::getUserId, userId);

            if (detail.getDishId() != null) {
                cartQuery.eq(ShoppingCart::getDishId, detail.getDishId());
                if (detail.getDishFlavor() != null) {
                    cartQuery.eq(ShoppingCart::getDishFlavor, detail.getDishFlavor());
                }
            } else {
                cartQuery.eq(ShoppingCart::getSetmealId, detail.getSetmealId());
            }

            ShoppingCart existingCart = shoppingCartService.getOne(cartQuery);

            if (existingCart != null) {
                // 如果购物车中已存在，更新数量
                existingCart.setNumber(existingCart.getNumber() + detail.getNumber());
                shoppingCartService.updateById(existingCart);
            } else {
                // 如果购物车中不存在，新增
                ShoppingCart newCart = new ShoppingCart();
                newCart.setUserId(userId);
                newCart.setDishId(detail.getDishId());
                newCart.setSetmealId(detail.getSetmealId());
                newCart.setName(detail.getName());
                newCart.setImage(detail.getImage());
                newCart.setAmount(detail.getAmount());
                newCart.setNumber(detail.getNumber());
                newCart.setDishFlavor(detail.getDishFlavor());
                newCart.setCreateTime(LocalDateTime.now());
                shoppingCartService.save(newCart);
            }
        }

        log.info("再来一单成功，订单ID：{}，用户ID：{}", id, userId);
        return Result.success();
    }

    @Override
    public Result<Void> payOrder(Long id) {
        var order = baseMapper.selectById(id);
        if (order == null) {
            return Result.error("订单不存在");
        }

        if (order.getStatus() != OrderConstant.Status.PENDING_PAYMENT) {
            return Result.error("订单状态不是待付款");
        }

        order.setStatus(OrderConstant.Status.TO_BE_CONFIRMED);
        var res = baseMapper.updateById(order);
        if (res == 1) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", 1); // 1: 来单提醒 2. 催单提醒
            map.put("orderNumber", order.getNumber());
            // 转为json字符串
            String json = JSONUtil.toJsonStr(map);
            webSocketServer.sendAllMessage(json);
            return Result.success();
        }

        return Result.error("支付失败");
    }
}
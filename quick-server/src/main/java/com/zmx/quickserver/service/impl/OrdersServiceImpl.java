package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.Orders;
import com.zmx.quickserver.mapper.OrdersMapper;
import com.zmx.quickserver.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * 订单Service实现类
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
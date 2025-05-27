package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.OrderDetail;
import com.zmx.quickserver.mapper.OrderDetailMapper;
import com.zmx.quickserver.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * 订单明细Service实现类
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
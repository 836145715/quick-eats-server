package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.ShoppingCart;
import com.zmx.quickserver.mapper.ShoppingCartMapper;
import com.zmx.quickserver.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * 购物车Service实现类
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
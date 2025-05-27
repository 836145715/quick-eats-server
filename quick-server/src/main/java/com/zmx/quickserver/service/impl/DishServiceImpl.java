package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickserver.mapper.DishMapper;
import com.zmx.quickserver.service.DishService;
import org.springframework.stereotype.Service;

/**
 * 菜品Service实现类
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

}
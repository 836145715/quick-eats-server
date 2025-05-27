package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.SetmealDish;
import com.zmx.quickserver.mapper.SetmealDishMapper;
import com.zmx.quickserver.service.SetmealDishService;
import org.springframework.stereotype.Service;

/**
 * 套餐菜品关系Service实现类
 */
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
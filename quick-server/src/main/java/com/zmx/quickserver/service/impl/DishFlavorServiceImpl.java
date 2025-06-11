package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.DishFlavor;
import com.zmx.quickserver.mapper.DishFlavorMapper;
import com.zmx.quickserver.service.DishFlavorService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜品口味Service实现类
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

    @Override
    @Cacheable(value = "dishFlavor", key = "#dishId", unless = "#result == null || #result.isEmpty()")
    public List<DishFlavor> listByDishId(Long dishId) {
        var query = lambdaQuery();
        query.eq(DishFlavor::getDishId, dishId);
        return query.list();
    }
}
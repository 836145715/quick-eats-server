package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.quickpojo.entity.DishFlavor;

import java.util.List;

/**
 * 菜品口味Service接口
 */
public interface DishFlavorService extends IService<DishFlavor> {
    List<DishFlavor> listByDishId(Long dishId);
}
package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.DishFlavor;
import com.zmx.quickserver.mapper.DishFlavorMapper;
import com.zmx.quickserver.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * 菜品口味Service实现类
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
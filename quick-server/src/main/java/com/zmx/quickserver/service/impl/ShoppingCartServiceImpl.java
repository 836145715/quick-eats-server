package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.response.Result;
import com.zmx.common.utils.BaseContext;
import com.zmx.quickpojo.dto.ShoppingCartDTO;
import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickpojo.entity.Setmeal;
import com.zmx.quickpojo.entity.ShoppingCart;
import com.zmx.quickpojo.vo.ShoppingCartVO;
import com.zmx.quickserver.mapper.ShoppingCartMapper;
import com.zmx.quickserver.service.DishService;
import com.zmx.quickserver.service.SetmealService;
import com.zmx.quickserver.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车Service实现类
 */
@Slf4j
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 添加商品到购物车
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    @Override
    public Result<Void> add(ShoppingCartDTO shoppingCartDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("添加购物车，用户ID：{}，请求参数：{}", userId, shoppingCartDTO);

        // 判断当前加入购物车的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);

        // 查询当前商品是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        if (shoppingCart.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
            queryWrapper.eq(shoppingCart.getDishFlavor() != null, ShoppingCart::getDishFlavor,
                    shoppingCart.getDishFlavor());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        List<ShoppingCart> existingItems = this.list(queryWrapper);

        if (existingItems != null && !existingItems.isEmpty()) {
            // 如果已经存在，就更新数量
            ShoppingCart cartItem = existingItems.get(0);
            cartItem.setNumber(cartItem.getNumber() + shoppingCartDTO.getNumber());
            this.updateById(cartItem);
        } else {
            // 如果不存在，添加到购物车，数量默认就是1
            // 判断当前添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 添加到购物车的是菜品
                Dish dish = dishService.getById(dishId);
                if (dish == null) {
                    return Result.error("菜品不存在");
                }
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 添加到购物车的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                if (setmealId == null) {
                    return Result.error("请选择要添加的商品");
                }
                Setmeal setmeal = setmealService.getById(setmealId);
                if (setmeal == null) {
                    return Result.error("套餐不存在");
                }
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(shoppingCartDTO.getNumber());
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
        }

        return Result.success();
    }

    /**
     * 查看购物车列表
     *
     * @return 购物车列表
     */
    @Override
    public Result<List<ShoppingCartVO>> listCart() {
        Long userId = BaseContext.getCurrentId();
        log.info("查看购物车，用户ID：{}", userId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> shoppingCarts = this.list(queryWrapper);

        List<ShoppingCartVO> cartVOList = shoppingCarts.stream().map(cart -> {
            ShoppingCartVO cartVO = new ShoppingCartVO();
            BeanUtils.copyProperties(cart, cartVO);
            return cartVO;
        }).collect(Collectors.toList());

        return Result.success(cartVOList);
    }

    /**
     * 修改购物车商品数量
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    @Override
    public Result<Void> update(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("修改购物车商品数量，用户ID：{}，请求参数：{}", userId, shoppingCartDTO);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        if (shoppingCartDTO.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCartDTO.getDishId());
            queryWrapper.eq(shoppingCartDTO.getDishFlavor() != null, ShoppingCart::getDishFlavor,
                    shoppingCartDTO.getDishFlavor());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCartDTO.getSetmealId());
        }

        ShoppingCart shoppingCart = this.getOne(queryWrapper);
        if (shoppingCart == null) {
            return Result.error("购物车中没有该商品");
        }

        shoppingCart.setNumber(shoppingCartDTO.getNumber());
        this.updateById(shoppingCart);

        return Result.success();
    }

    /**
     * 删除购物车商品（减少数量）
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    @Override
    public Result<Void> sub(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        log.info("删除购物车商品，用户ID：{}，请求参数：{}", userId, shoppingCartDTO);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        if (shoppingCartDTO.getDishId() != null) {
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCartDTO.getDishId());
            queryWrapper.eq(shoppingCartDTO.getDishFlavor() != null, ShoppingCart::getDishFlavor,
                    shoppingCartDTO.getDishFlavor());
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCartDTO.getSetmealId());
        }

        ShoppingCart shoppingCart = this.getOne(queryWrapper);
        if (shoppingCart == null) {
            return Result.error("购物车中没有该商品");
        }

        Integer currentNumber = shoppingCart.getNumber();
        if (currentNumber <= 1) {
            // 如果数量小于等于1，直接删除
            this.removeById(shoppingCart.getId());
        } else {
            // 否则数量减1
            shoppingCart.setNumber(currentNumber - 1);
            this.updateById(shoppingCart);
        }

        return Result.success();
    }

    /**
     * 清空购物车
     *
     * @return 操作结果
     */
    @Override
    public Result<Void> clean() {
        Long userId = BaseContext.getCurrentId();
        log.info("清空购物车，用户ID：{}", userId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        this.remove(queryWrapper);

        return Result.success();
    }

    @Override
    public Result<Long> counts() {
        Long userId = BaseContext.getCurrentId();
        log.info("获取购物车商品数量，用户ID：{}", userId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);

        return Result.success(this.count(queryWrapper));
    }
}
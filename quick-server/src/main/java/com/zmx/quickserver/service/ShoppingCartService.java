package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.ShoppingCartDTO;
import com.zmx.quickpojo.entity.ShoppingCart;
import com.zmx.quickpojo.vo.ShoppingCartVO;

import java.util.List;

/**
 * 购物车Service接口
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加商品到购物车
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    Result<Void> add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车列表
     *
     * @return 购物车列表
     */
    Result<List<ShoppingCartVO>> listCart();

    /**
     * 修改购物车商品数量
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    Result<Void> update(ShoppingCartDTO shoppingCartDTO);

    /**
     * 删除购物车商品
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    Result<Void> sub(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     *
     * @return 操作结果
     */
    Result<Void> clean();

    /**
     * 获取购物车商品数量
     *
     * @return 购物车商品数量
     */
    Result<Long> counts();
}
package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.DishAddReqDTO;
import com.zmx.quickpojo.dto.DishPageListReqDTO;
import com.zmx.quickpojo.dto.DishStatusDTO;
import com.zmx.quickpojo.entity.Dish;

import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     * @return 操作结果
     */
    Result<Void> add(DishAddReqDTO dishDTO);

    /**
     * 菜品分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    PageResult pageList(DishPageListReqDTO dto);

    /**
     * 删除菜品
     *
     * @param id 菜品ID
     * @return 操作结果
     */
    Result deleteById(long id);

    /**
     * 更新菜品状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    Result updateStatus(DishStatusDTO statusDTO);

    /**
     * 更新菜品信息
     *
     * @param dishDTO 菜品信息
     * @return 操作结果
     */
    Result update(DishAddReqDTO dishDTO);

    /**
     * 根据分类ID查询菜品列表
     *
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    Result<List<Dish>> listByCategoryId(Long categoryId);
}
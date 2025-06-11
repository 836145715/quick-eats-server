package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.SetmealAddReqDTO;
import com.zmx.quickpojo.dto.SetmealPageListReqDTO;
import com.zmx.quickpojo.dto.SetmealStatusDTO;
import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickpojo.entity.Setmeal;
import com.zmx.quickpojo.vo.SetmealDishVO;

import java.util.List;

/**
 * 套餐服务接口
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐信息
     * @return 操作结果
     */
    Result<Void> add(SetmealAddReqDTO setmealDTO);

    /**
     * 套餐分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    PageResult pageList(SetmealPageListReqDTO dto);

    /**
     * 删除套餐
     *
     * @param id 套餐ID
     * @return 操作结果
     */
    Result deleteById(long id);

    /**
     * 更新套餐状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    Result updateStatus(SetmealStatusDTO statusDTO);

    /**
     * 更新套餐信息
     *
     * @param setmealDTO 套餐信息
     * @return 操作结果
     */
    Result update(SetmealAddReqDTO setmealDTO);

    /**
     * 根据分类ID查询套餐列表
     *
     * @param categoryId 分类ID
     * @return 套餐列表
     */
    Result<List<Setmeal>> listByCategoryId(Long categoryId);

    /**
     * 根据套餐ID查询菜品
     *
     * @param id 套餐ID
     * @return 菜品列表
     */
    Result<List<SetmealDishVO>> getDish(Long id);
}
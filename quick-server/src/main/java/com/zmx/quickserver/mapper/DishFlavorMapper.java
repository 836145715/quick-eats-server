package com.zmx.quickserver.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.quickpojo.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜品口味Mapper接口
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {

    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> selectByDishId(Long dishId);

    /**
     * 根据菜品ID查询口味
     * @param dishIds
     * @return
     */
    default List<DishFlavor> selectByDishIds(List<Long> dishIds){
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, dishIds);
        return selectList(queryWrapper);
    }
}
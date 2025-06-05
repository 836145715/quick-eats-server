package com.zmx.quickserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.quickpojo.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 套餐菜品关系Mapper接口
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from setmeal where category_id =  #{categoryId}")
    Integer countByCategoryId(Long categoryId);


}
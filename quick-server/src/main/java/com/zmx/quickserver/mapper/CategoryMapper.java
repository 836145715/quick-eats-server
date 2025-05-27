package com.zmx.quickserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.quickpojo.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类Mapper接口
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
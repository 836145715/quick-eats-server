package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.Category;
import com.zmx.quickserver.mapper.CategoryMapper;
import com.zmx.quickserver.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * 分类Service实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
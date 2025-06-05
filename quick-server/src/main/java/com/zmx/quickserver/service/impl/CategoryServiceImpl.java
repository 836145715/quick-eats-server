package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.constants.StatusConstant;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.common.utils.BaseContext;
import com.zmx.quickpojo.dto.CategoryAddReqDTO;
import com.zmx.quickpojo.dto.CategoryPageListReqDTO;
import com.zmx.quickpojo.dto.CategoryStatusDTO;
import com.zmx.quickpojo.entity.Category;
import com.zmx.quickpojo.vo.CategoryPageListRspVO;
import com.zmx.quickserver.mapper.CategoryMapper;
import com.zmx.quickserver.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类Service实现类
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public Result<Void> add(CategoryAddReqDTO categoryDTO) {
        // 对象属性拷贝
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 设置状态为启用
        category.setStatus(StatusConstant.ENABLE);

        // 保存分类（创建时间、更新时间、创建人、更新人会由MyBatisPlus自动填充）
        boolean success = save(category);
        if (success) {
            return Result.success();
        }

        return Result.error("添加分类失败");
    }

    @Override
    public PageResult pageList(CategoryPageListReqDTO dto) {
        // 构建查询条件
        LambdaQueryChainWrapper<Category> query = lambdaQuery();
        query.like(StringUtils.isNotBlank(dto.getName()), Category::getName, dto.getName());
        query.eq(dto.getType() != null, Category::getType, dto.getType());

        // 按照排序字段升序排列
        query.orderByAsc(Category::getSort);

        // 分页查询
        Page<Category> page = new Page<>(dto.getCurrent(), dto.getSize());
        IPage<Category> pageResult = query.page(page);

        // 转VO
        List<Category> records = pageResult.getRecords();
        List<CategoryPageListRspVO> vos = records.stream().map(item -> CategoryPageListRspVO.builder()
                .id(item.getId())
                .name(item.getName())
                .type(item.getType())
                .sort(item.getSort())
                .status(item.getStatus())
                .build()).toList();

        return PageResult.success(pageResult, vos);
    }

    @Override
    public Result deleteById(long id) {
        // TODO: 后续需要检查该分类是否关联了菜品或套餐，如果已关联则不能删除

        boolean success = removeById(id);
        if (success) {
            return Result.success();
        }

        return Result.error("删除分类失败");
    }

    @Override
    public Result updateStatus(CategoryStatusDTO statusDTO) {
        // 根据ID查询分类
        Category category = getById(statusDTO.getId());
        if (category == null) {
            return Result.error("分类不存在");
        }

        // 更新状态（更新时间和更新人会由MyBatisPlus自动填充）
        category.setStatus(statusDTO.getStatus());

        // 执行更新
        boolean success = updateById(category);
        if (success) {
            return Result.success();
        }

        return Result.error("更新分类状态失败");
    }

    @Override
    public Result update(CategoryAddReqDTO categoryDTO) {
        // 根据ID查询分类
        Category category = getById(categoryDTO.getId());
        if (category == null) {
            return Result.error("分类不存在");
        }

        // 属性拷贝（更新时间和更新人会由MyBatisPlus自动填充）
        BeanUtils.copyProperties(categoryDTO, category);

        // 执行更新
        boolean success = updateById(category);
        if (success) {
            return Result.success();
        }

        return Result.error("更新分类信息失败");
    }
}
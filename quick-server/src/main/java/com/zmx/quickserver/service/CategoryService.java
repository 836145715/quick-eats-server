package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.CategoryAddReqDTO;
import com.zmx.quickpojo.dto.CategoryPageListReqDTO;
import com.zmx.quickpojo.dto.CategoryStatusDTO;
import com.zmx.quickpojo.entity.Category;

import java.util.List;

/**
 * 分类Service接口
 */
public interface CategoryService extends IService<Category> {

    /**
     * 新增分类
     *
     * @param categoryDTO 分类信息
     * @return 操作结果
     */
    Result<Void> add(CategoryAddReqDTO categoryDTO);

    /**
     * 分类分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    PageResult pageList(CategoryPageListReqDTO dto);

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 操作结果
     */
    Result deleteById(long id);

    /**
     * 更新分类状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    Result updateStatus(CategoryStatusDTO statusDTO);

    /**
     * 更新分类信息
     *
     * @param categoryDTO 分类信息
     * @return 操作结果
     */
    Result update(CategoryAddReqDTO categoryDTO);

    /**
     * 根据分类类型列出分类
     *
     * @param type 分类类型
     * @return 分类列表
     */
    Result<List<Category>> listByType(Integer type);
}
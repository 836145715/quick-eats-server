package com.zmx.quickserver.controller;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.CategoryAddReqDTO;
import com.zmx.quickpojo.dto.CategoryPageListReqDTO;
import com.zmx.quickpojo.dto.CategoryStatusDTO;
import com.zmx.quickserver.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/category")
@Tag(name = "分类管理", description = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     */
    @PostMapping("/add")
    @ApiLog
    @Operation(summary = "新增分类", description = "新增分类接口")
    public Result add(@RequestBody @Valid CategoryAddReqDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        return categoryService.add(categoryDTO);
    }

    /**
     * 分类分页查询
     */
    @PostMapping("/pageList")
    @ApiLog
    @Operation(summary = "分类分页查询", description = "分类分页查询接口")
    public PageResult pageList(@RequestBody @Valid CategoryPageListReqDTO categoryPageListDTO) {
        log.info("分类分页查询：{}", categoryPageListDTO);
        return categoryService.pageList(categoryPageListDTO);
    }

    /**
     * 删除分类
     */
    @PostMapping("/delete/{id}")
    @ApiLog
    @Operation(summary = "删除分类", description = "删除分类接口")
    public Result delete(@PathVariable long id) {
        log.info("删除分类：{}", id);
        return categoryService.deleteById(id);
    }

    /**
     * 更新分类状态
     */
    @PostMapping("/status")
    @ApiLog
    @Operation(summary = "更新分类状态", description = "更新分类状态接口")
    public Result updateStatus(@RequestBody @Valid CategoryStatusDTO statusDTO) {
        log.info("更新分类状态：{}", statusDTO);
        return categoryService.updateStatus(statusDTO);
    }

    /**
     * 更新分类信息
     */
    @PostMapping("/update")
    @ApiLog
    @Operation(summary = "更新分类信息", description = "更新分类信息接口")
    public Result update(@RequestBody @Valid CategoryAddReqDTO categoryDTO) {
        log.info("更新分类信息：{}", categoryDTO);
        return categoryService.update(categoryDTO);
    }
}
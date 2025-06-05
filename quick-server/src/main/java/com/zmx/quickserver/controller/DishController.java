package com.zmx.quickserver.controller;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.DishAddReqDTO;
import com.zmx.quickpojo.dto.DishPageListReqDTO;
import com.zmx.quickpojo.dto.DishStatusDTO;
import com.zmx.quickpojo.entity.Dish;
import com.zmx.quickserver.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品控制器
 */
@Slf4j
@RestController
@RequestMapping("/dish")
@Tag(name = "菜品管理", description = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDTO 菜品信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @ApiLog
    @Operation(summary = "新增菜品", description = "新增菜品接口")
    public Result<Void> add(@RequestBody @Valid DishAddReqDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO.getName());
        return dishService.add(dishDTO);
    }

    /**
     * 菜品分页查询
     *
     * @param dishPageListDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/pageList")
    @ApiLog
    @Operation(summary = "菜品分页查询", description = "菜品分页查询接口")
    public PageResult pageList(@RequestBody @Valid DishPageListReqDTO dishPageListDTO) {
        return dishService.pageList(dishPageListDTO);
    }

    /**
     * 删除菜品
     *
     * @param id 菜品ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @ApiLog
    @Operation(summary = "删除菜品", description = "删除菜品接口")
    public Result<Void> delete(@PathVariable long id) {
        log.info("删除菜品：{}", id);
        return dishService.deleteById(id);
    }

    /**
     * 更新菜品状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @PostMapping("/status")
    @ApiLog
    @Operation(summary = "更新菜品状态", description = "更新菜品状态接口")
    public Result<Void> updateStatus(@RequestBody @Valid DishStatusDTO statusDTO) {
        log.info("更新菜品状态：{}", statusDTO);
        return dishService.updateStatus(statusDTO);
    }

    /**
     * 更新菜品信息
     *
     * @param dishDTO 菜品信息
     * @return 操作结果
     */
    @PostMapping("/update")
    @ApiLog
    @Operation(summary = "更新菜品信息", description = "更新菜品信息接口")
    public Result<Void> update(@RequestBody @Valid DishAddReqDTO dishDTO) {
        log.info("更新菜品信息：{}", dishDTO.getId());
        return dishService.update(dishDTO);
    }

    /**
     * 根据分类ID查询菜品列表
     *
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    @GetMapping("/listByCategoryId")
    @ApiLog
    @Operation(summary = "根据分类ID查询菜品列表", description = "根据分类ID查询菜品列表接口")
    public Result<List<Dish>> listByCategoryId(@RequestParam Long categoryId) {
        return dishService.listByCategoryId(categoryId);
    }
}
package com.zmx.quickserver.controller;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.SetmealAddReqDTO;
import com.zmx.quickpojo.dto.SetmealPageListReqDTO;
import com.zmx.quickpojo.dto.SetmealStatusDTO;
import com.zmx.quickpojo.entity.Setmeal;
import com.zmx.quickserver.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐控制器
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
@Tag(name = "套餐管理", description = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @param setmealDTO 套餐信息
     * @return 操作结果
     */
    @PostMapping("/add")
    @ApiLog
    @Operation(summary = "新增套餐", description = "新增套餐接口")
    public Result<Void> add(@RequestBody @Valid SetmealAddReqDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO.getName());
        return setmealService.add(setmealDTO);
    }

    /**
     * 套餐分页查询
     *
     * @param setmealPageListDTO 查询条件
     * @return 分页结果
     */
    @PostMapping("/pageList")
    @ApiLog
    @Operation(summary = "套餐分页查询", description = "套餐分页查询接口")
    public PageResult pageList(@RequestBody @Valid SetmealPageListReqDTO setmealPageListDTO) {
        return setmealService.pageList(setmealPageListDTO);
    }

    /**
     * 删除套餐
     *
     * @param id 套餐ID
     * @return 操作结果
     */
    @PostMapping("/delete/{id}")
    @ApiLog
    @Operation(summary = "删除套餐", description = "删除套餐接口")
    public Result<Void> delete(@PathVariable long id) {
        log.info("删除套餐：{}", id);
        return setmealService.deleteById(id);
    }

    /**
     * 更新套餐状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    @PostMapping("/status")
    @ApiLog
    @Operation(summary = "更新套餐状态", description = "更新套餐状态接口")
    public Result<Void> updateStatus(@RequestBody @Valid SetmealStatusDTO statusDTO) {
        log.info("更新套餐状态：{}", statusDTO);
        return setmealService.updateStatus(statusDTO);
    }

    /**
     * 更新套餐信息
     *
     * @param setmealDTO 套餐信息
     * @return 操作结果
     */
    @PostMapping("/update")
    @ApiLog
    @Operation(summary = "更新套餐信息", description = "更新套餐信息接口")
    public Result<Void> update(@RequestBody @Valid SetmealAddReqDTO setmealDTO) {
        log.info("更新套餐信息：{}", setmealDTO.getId());
        return setmealService.update(setmealDTO);
    }

    /**
     * 根据分类ID查询套餐列表
     *
     * @param categoryId 分类ID
     * @return 套餐列表
     */
    @GetMapping("/listByCategoryId")
    @ApiLog
    @Operation(summary = "根据分类ID查询套餐列表", description = "根据分类ID查询套餐列表接口")
    public Result<List<Setmeal>> listByCategoryId(@RequestParam Long categoryId) {
        return setmealService.listByCategoryId(categoryId);
    }
}
package com.zmx.quickserver.controller.user;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.entity.Category;
import com.zmx.quickpojo.vo.DishMobileRspVO;
import com.zmx.quickserver.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/user/dish")
@Tag(name = "菜品管理", description = "菜品相关接口")
public class UserDishController {

    @Autowired
    private DishService dishService;

    /**
     * 根据分类类型列出分类
     */
    @GetMapping("/list")
    @ApiLog
    @Operation(summary = "获取移动端菜品列表", description = "获取移动端菜品列表")
    public Result<List<DishMobileRspVO>> list() {
        log.info("根据分类类型列出分类");
        return dishService.listMobile();
    }
}
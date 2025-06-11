package com.zmx.quickserver.controller.user;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.SetmealAddReqDTO;
import com.zmx.quickpojo.dto.SetmealPageListReqDTO;
import com.zmx.quickpojo.dto.SetmealStatusDTO;
import com.zmx.quickpojo.vo.SetmealDishVO;
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
@RequestMapping("/user/setmeal")
@Tag(name = "套餐管理", description = "套餐相关接口")
public class UserSetmealController {

    @Autowired
    private SetmealService setmealService;

    // 获取菜品数据
    @GetMapping("/dish/{id}")
    @ApiLog
    @Operation(summary = "根据套餐ID查询菜品", description = "根据套餐ID查询菜品")
    public Result<List<SetmealDishVO>> getDish(@PathVariable("id") Long id) {
        return setmealService.getDish(id);
    }

}
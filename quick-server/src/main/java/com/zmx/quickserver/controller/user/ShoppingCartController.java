package com.zmx.quickserver.controller.user;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.ShoppingCartDTO;
import com.zmx.quickpojo.vo.ShoppingCartVO;
import com.zmx.quickserver.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Tag(name = "购物车管理", description = "购物车相关接口")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加商品到购物车
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    @PostMapping("/add")
    @ApiLog
    @Operation(summary = "添加商品到购物车", description = "添加菜品或套餐到购物车")
    public Result<Void> add(@RequestBody @Valid ShoppingCartDTO shoppingCartDTO) {
        log.info("添加商品到购物车：{}", shoppingCartDTO);
        return shoppingCartService.add(shoppingCartDTO);
    }

    /**
     * 查看购物车列表
     *
     * @return 购物车列表
     */
    @GetMapping("/list")
    @ApiLog
    @Operation(summary = "查看购物车列表", description = "获取当前用户的购物车商品列表")
    public Result<List<ShoppingCartVO>> list() {
        log.info("查看购物车列表");
        return shoppingCartService.listCart();
    }

    /**
     * 修改购物车商品数量
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    @PostMapping("/update")
    @ApiLog
    @Operation(summary = "修改购物车商品数量", description = "直接设置购物车中商品的数量")
    public Result<Void> update(@RequestBody @Valid ShoppingCartDTO shoppingCartDTO) {
        log.info("修改购物车商品数量：{}", shoppingCartDTO);
        return shoppingCartService.update(shoppingCartDTO);
    }

    /**
     * 删除购物车商品（减少数量）
     *
     * @param shoppingCartDTO 购物车请求参数
     * @return 操作结果
     */
    @PostMapping("/sub")
    @ApiLog
    @Operation(summary = "删除购物车商品", description = "减少购物车中商品的数量，数量为1时直接删除")
    public Result<Void> sub(@RequestBody @Valid ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车商品：{}", shoppingCartDTO);
        return shoppingCartService.sub(shoppingCartDTO);
    }

    /**
     * 清空购物车
     *
     * @return 操作结果
     */
    @GetMapping("/clean")
    @ApiLog
    @Operation(summary = "清空购物车", description = "清空当前用户的所有购物车商品")
    public Result<Void> clean() {
        log.info("清空购物车");
        return shoppingCartService.clean();
    }

    @GetMapping("/count")
    @ApiLog
    @Operation(summary = "获取购物车商品数量", description = "获取当前用户的购物车商品数量")
    public Result<Long> count() {
        return Result.success(shoppingCartService.count());
    }

}

package com.zmx.quickserver.controller.admin;

import com.zmx.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/shop")
@Tag(name = "后台店铺管理")
@Slf4j
public class AdminShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/setStatus")
    @Operation(summary = "店铺状态设置")
    public Result setStatus(Integer status) {
        log.info("设置店铺状态: {}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set("shop_status", status);
        return Result.success();
    }

    @GetMapping("/getStatus")
    @Operation(summary = "获取店铺状态")
    public Result getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get("shop_status");
        return Result.success(status);
    }

}

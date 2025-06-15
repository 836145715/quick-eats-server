package com.zmx.quickserver.controller.user;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.WechatLoginReqDTO;
import com.zmx.quickpojo.vo.UserInfoVO;
import com.zmx.quickpojo.vo.WechatLoginRspVO;
import com.zmx.quickserver.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 微信小程序登录
     *
     * @param loginDTO 登录请求参数
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiLog
    @Operation(summary = "微信小程序登录", description = "通过微信小程序code进行用户登录")
    public Result<WechatLoginRspVO> wechatLogin(@RequestBody @Valid WechatLoginReqDTO loginDTO) {
        log.info("微信小程序登录请求，code: {}", loginDTO.getCode());
        return userService.wechatLogin(loginDTO);
    }

    /**
     * 用户退出登录
     *
     * @return 操作结果
     */
    @PostMapping("/logout")
    @ApiLog
    @Operation(summary = "用户退出登录", description = "用户退出登录接口")
    public Result<Void> logout() {
        log.info("用户退出登录");
        return Result.success();
    }

    @GetMapping("/info")
    @ApiLog
    @Operation(summary = "获取用户信息", description = "获取用户信息接口")
    public Result<UserInfoVO> getUserInfo() {
        log.info("获取用户信息");
        return Result.success(userService.getUserInfo());
    }

}

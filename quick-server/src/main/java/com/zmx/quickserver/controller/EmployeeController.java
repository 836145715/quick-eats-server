package com.zmx.quickserver.controller;

import com.zmx.common.annotation.ApiLog;
import com.zmx.quickpojo.dto.EmployeeDTO;
import com.zmx.quickpojo.vo.LoginDTO;
import com.zmx.quickpojo.vo.LoginResponseDTO;
import com.zmx.common.response.Result;
import com.zmx.quickserver.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工控制器
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Tag(name = "员工管理", description = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiLog
    @Operation(summary = "员工登录", description = "员工登录接口")
    public Result<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        log.info("员工登录：{}", loginDTO.getUsername());
        return employeeService.login(loginDTO);
    }

    @PostMapping("/logout")
    @ApiLog
    @Operation(summary = "员工退出", description = "员工退出接口")
    public Result<Void> logout() {
        return Result.success();
    }
//
//    @PostMapping("/add")
//    @ApiLog
//    @Operation(summary = "新增员工", description = "新增员工接口")
//    public Result<Void> add(@RequestBody @Valid EmployeeDTO employeeDTO) {
//        return employeeService.add(employeeDTO);
//    }

}
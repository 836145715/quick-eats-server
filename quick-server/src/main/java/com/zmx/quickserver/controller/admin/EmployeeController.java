package com.zmx.quickserver.controller.admin;

import com.zmx.common.annotation.ApiLog;
import com.zmx.common.response.PageResult;
import com.zmx.quickpojo.dto.EmployeeAddReqDTO;
import com.zmx.quickpojo.dto.EmployeePageListReqDTO;
import com.zmx.quickpojo.dto.EmployeeStatusDTO;
import com.zmx.quickpojo.dto.LoginReqDTO;
import com.zmx.quickpojo.vo.LoginRspVO;
import com.zmx.common.response.Result;
import com.zmx.quickserver.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<LoginRspVO> login(@RequestBody @Valid LoginReqDTO loginDTO) {
        log.info("员工登录：{}", loginDTO.getUsername());
        return employeeService.login(loginDTO);
    }

    @PostMapping("/logout")
    @ApiLog
    @Operation(summary = "员工退出", description = "员工退出接口")
    public Result<Void> logout() {
        return Result.success();
    }

    @PostMapping("/add")
    @ApiLog
    @Operation(summary = "新增员工", description = "新增员工接口")
    public Result add(@RequestBody @Valid EmployeeAddReqDTO employeeDTO) {
        return employeeService.add(employeeDTO);
    }

    @PostMapping("/pageList")
    @ApiLog
    @Operation(summary = "员工分页查询", description = "员工分页查询接口")
    public PageResult pageList(@RequestBody @Valid EmployeePageListReqDTO employeePageListDTO) {
        return employeeService.pageList(employeePageListDTO);
    }

    @PostMapping("/delete/{id}")
    @ApiLog
    @Operation(summary = "删除员工", description = "删除员工接口")
    public Result delete(@PathVariable long id) {
        return employeeService.deleteById(id);
    }

    @PostMapping("/status")
    @ApiLog
    @Operation(summary = "更新员工状态", description = "更新员工状态接口")
    public Result updateStatus(@RequestBody @Valid EmployeeStatusDTO statusDTO) {
        return employeeService.updateStatus(statusDTO);
    }

    @PostMapping("/update")
    @ApiLog
    @Operation(summary = "更新员工信息", description = "更新员工信息接口")
    public Result update(@RequestBody @Valid EmployeeAddReqDTO employeeDTO) {
        return employeeService.update(employeeDTO);
    }
}
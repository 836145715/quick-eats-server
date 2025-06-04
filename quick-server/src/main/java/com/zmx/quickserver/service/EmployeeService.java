package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.EmployeeDTO;
import com.zmx.quickpojo.dto.EmployeePageListReqDTO;
import com.zmx.quickpojo.entity.Employee;
import com.zmx.quickpojo.dto.LoginReqDTO;
import com.zmx.quickpojo.vo.LoginRspVO;

/**
 * 员工服务接口
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    Result<LoginRspVO> login(LoginReqDTO loginDTO);

    Result<Void> add(EmployeeDTO employeeDTO);

    PageResult pageList(EmployeePageListReqDTO dto);

    Result deleteById(long id);
}
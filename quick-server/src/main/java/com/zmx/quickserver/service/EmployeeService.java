package com.zmx.quickserver.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.entity.Employee;
import com.zmx.quickpojo.vo.LoginDTO;
import com.zmx.quickpojo.vo.LoginResponseDTO;

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
    Result<LoginResponseDTO> login(LoginDTO loginDTO);
}
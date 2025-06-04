package com.zmx.quickserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.EmployeeAddReqDTO;
import com.zmx.quickpojo.dto.EmployeePageListReqDTO;
import com.zmx.quickpojo.dto.EmployeeStatusDTO;
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

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息
     * @return 操作结果
     */
    Result<Void> add(EmployeeAddReqDTO employeeDTO);

    /**
     * 员工分页查询
     *
     * @param dto 查询参数
     * @return 分页结果
     */
    PageResult pageList(EmployeePageListReqDTO dto);

    /**
     * 删除员工
     *
     * @param id 员工ID
     * @return 操作结果
     */
    Result deleteById(long id);

    /**
     * 更新员工状态
     *
     * @param statusDTO 状态信息
     * @return 操作结果
     */
    Result updateStatus(EmployeeStatusDTO statusDTO);

    /**
     * 更新员工信息
     *
     * @param employeeDTO 员工信息
     * @return 操作结果
     */
    Result update(EmployeeAddReqDTO employeeDTO);
}
package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.enums.ErrorCodeEnum;
import com.zmx.common.exception.BusinessException;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.entity.Employee;
import com.zmx.quickserver.mapper.EmployeeMapper;
import com.zmx.quickpojo.vo.LoginDTO;
import com.zmx.quickpojo.vo.LoginResponseDTO;
import com.zmx.quickserver.service.EmployeeService;
import com.zmx.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 员工服务实现类
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 员工登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @Override
    public Result<LoginResponseDTO> login(LoginDTO loginDTO) {
        // 1. 将页面提交的密码进行md5加密处理
        String password = DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes());

        // 2. 根据用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, loginDTO.getUsername());
        Employee employee = this.getOne(queryWrapper);

        // 3. 如果没有查询到则返回登录失败结果
        if (employee == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_EXIST);
        }

        // 4. 密码比对，如果不一致则返回登录失败结果
        if (!employee.getPassword().equals(password)) {
            throw new BusinessException(ErrorCodeEnum.PASSWORD_ERROR);
        }

        // 5. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (employee.getStatus() == 0) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_DISABLED);
        }

        // 6. 登录成功，生成token
        String token = jwtUtils.generateToken(employee.getId());

        // 7. 构建登录响应DTO
        LoginResponseDTO loginResponse = LoginResponseDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .username(employee.getUsername())
                .token(token)
                .build();

        return Result.success(loginResponse);
    }
}
package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.common.constants.StatusConstant;
import com.zmx.common.enums.ErrorCodeEnum;
import com.zmx.common.exception.BusinessException;
import com.zmx.common.properties.JwtAdminProperties;
import com.zmx.common.response.PageResult;
import com.zmx.common.response.Result;
import com.zmx.quickpojo.dto.EmployeeAddReqDTO;
import com.zmx.quickpojo.dto.EmployeePageListReqDTO;
import com.zmx.quickpojo.dto.EmployeeStatusDTO;
import com.zmx.quickpojo.entity.Employee;
import com.zmx.quickpojo.vo.EmployeePageListRspVO;
import com.zmx.quickserver.mapper.EmployeeMapper;
import com.zmx.quickpojo.dto.LoginReqDTO;
import com.zmx.quickpojo.vo.LoginRspVO;
import com.zmx.quickserver.service.EmployeeService;
import com.zmx.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

/**
 * 员工服务实现类
 */
@Slf4j
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private JwtAdminProperties jwtAdminProperties;


    /**
     * 员工登录
     *
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @Override
    public Result<LoginRspVO> login(LoginReqDTO loginDTO) {
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
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new BusinessException(ErrorCodeEnum.ACCOUNT_DISABLED);
        }

        // 6. 登录成功，生成token
        String token = JwtUtils.configure(jwtAdminProperties).generateToken(employee.getId());

        // 7. 构建登录响应DTO
        LoginRspVO loginResponse = LoginRspVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .username(employee.getUsername())
                .token(token)
                .build();

        return Result.success(loginResponse);
    }

    @Override
    public Result<Void> add(EmployeeAddReqDTO employeeDTO) {
        // 对象属性拷贝
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setStatus(StatusConstant.ENABLE);

        // 默认123456
        String password = "123456";
        String passwordEn = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(passwordEn);

        // 保存员工（创建时间、更新时间、创建人、更新人会由MyBatisPlus自动填充）
        int bRet = baseMapper.insert(employee);
        if (bRet > 0) {
            return Result.success();
        }

        return Result.error("添加员工失败！");
    }

    @Override
    public PageResult pageList(EmployeePageListReqDTO dto) {

        LambdaQueryChainWrapper<Employee> query = lambdaQuery();
        query.like(StringUtils.isNotBlank(dto.getUsername()), Employee::getUsername, dto.getUsername());
        query.like(StringUtils.isNotBlank(dto.getName()), Employee::getName, dto.getName());
        query.like(StringUtils.isNotBlank(dto.getPhone()), Employee::getPhone, dto.getPhone());
        query.eq(dto.getSex() != null, Employee::getSex, dto.getSex());

        query.orderByDesc(Employee::getCreateTime);

        Page<Employee> page = new Page<>(dto.getCurrent(), dto.getSize());
        IPage<Employee> pageResult = query.page(page);

        // 转VO
        List<Employee> records = pageResult.getRecords();
        List<EmployeePageListRspVO> vos = records.stream().map(item -> EmployeePageListRspVO.builder()
                .id(item.getId())
                .name(item.getName())
                .username(item.getUsername())
                .phone(item.getPhone())
                .sex(item.getSex())
                .status(item.getStatus())
                .idNumber(item.getIdNumber())
                .build()).toList();

        return PageResult.success(pageResult, vos);
    }

    @Override
    public Result deleteById(long id) {
        int bRet = baseMapper.deleteById(id);
        if (bRet > 0) {
            return Result.success();
        }
        return Result.error("删除员工失败！");
    }

    @Override
    public Result updateStatus(EmployeeStatusDTO statusDTO) {
        // 1. 根据ID查询员工
        Employee employee = getById(statusDTO.getId());
        if (employee == null) {
            return Result.error("员工不存在");
        }

        // 2. 更新状态（更新时间和更新人会由MyBatisPlus自动填充）
        employee.setStatus(statusDTO.getStatus());

        // 3. 执行更新
        boolean success = updateById(employee);
        if (success) {
            return Result.success();
        }

        return Result.error("更新员工状态失败");
    }

    @Override
    public Result update(EmployeeAddReqDTO employeeDTO) {
        // 1. 根据ID查询员工
        Employee employee = getById(employeeDTO.getId());
        if (employee == null) {
            return Result.error("员工不存在");
        }

        // 2. 属性拷贝（更新时间和更新人会由MyBatisPlus自动填充）
        BeanUtils.copyProperties(employeeDTO, employee);

        // 3. 执行更新
        boolean success = updateById(employee);
        if (success) {
            return Result.success();
        }

        return Result.error("更新员工信息失败");
    }
}
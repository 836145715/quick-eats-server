package com.zmx.quickserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zmx.quickpojo.entity.User;
import com.zmx.quickserver.mapper.UserMapper;
import com.zmx.quickserver.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
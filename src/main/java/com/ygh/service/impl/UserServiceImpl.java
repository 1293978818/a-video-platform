package com.ygh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygh.domain.User;
import com.ygh.exception.BizException;
import com.ygh.mapper.UserMapper;
import com.ygh.service.UserService;

/**
 * @author ygh
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(String username,String password) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        user.setUsername(username);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        if (userMapper.selectOne(lambdaQueryWrapper) != null) {
            throw new BizException("该用户名已被注册");
        }
        userMapper.insert(user);
    }

    @Override
    public User selectById(String id) {
        if(id == null){
            throw new BizException("查询参数不能为空");
        }
        return userMapper.selectById(id);
    }
    
}

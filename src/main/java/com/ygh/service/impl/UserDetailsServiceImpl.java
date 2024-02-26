package com.ygh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygh.domain.MyUserDetails;
import com.ygh.domain.User;
import com.ygh.mapper.UserMapper;

/**
 * 处理用户登录的业务的实现类
 * @author ygh
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(lambdaQueryWrapper);

        if(user == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        MyUserDetails myUserDetails = new MyUserDetails(user);
        return myUserDetails;
    }
    
}

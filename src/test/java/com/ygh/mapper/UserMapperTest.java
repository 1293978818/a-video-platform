package com.ygh.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygh.domain.User;

@SpringBootTest
public class UserMapperTest {
    
    @Autowired
    private UserMapper userMapper;

    @Test
    public void update(){
        User user = new User();
        user.setUsername("ygh");
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        user.setVersion(3);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,user.getUsername());
        userMapper.update(user, lambdaQueryWrapper);
    }

    @Test
    public void getByUsername(){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, "ygh");
        System.out.println(userMapper.selectOne(lambdaQueryWrapper));
    }
}

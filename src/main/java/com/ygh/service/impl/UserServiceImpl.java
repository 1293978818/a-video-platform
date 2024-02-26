package com.ygh.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygh.domain.User;
import com.ygh.exception.BizException;
import com.ygh.mapper.UserMapper;
import com.ygh.service.UserService;

/**
 * 处理用户操作的实现类
 * @author ygh
 */
@Service
public class UserServiceImpl implements UserService{

    @Value("${my.avatar_address}")
    private String baseUrl;

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
            throw new BizException("id信息为空");
        }

        String nums = "\\d+";
        if(!id.matches(nums)){
            throw new BizException("id非法");
        }

        User user = userMapper.selectById(id);

        if(user == null){
            throw new BizException("userid不存在");
        }

        return user;
    }

    @Override
    public void insertAvatar(MultipartFile file, User user) throws IOException {
        if(file == null || file.isEmpty()){
            throw new BizException("文件不能为空");
        }

        String contentType = file.getContentType();
        String imageContentType = "image/";
        if(contentType == null || (!contentType.startsWith(imageContentType))){
            throw new BizException("该文件不是图片");
        }

        byte[] bytes = file.getBytes();
        String url = baseUrl + user.getId() + "." + contentType.split("/")[1];
        Path path = Paths.get(url);

        Files.write(path, bytes);
        userMapper.insertAvatar(url, user.getUsername());
    }
    
}

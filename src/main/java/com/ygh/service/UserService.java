package com.ygh.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ygh.domain.User;

/**
 * @author ygh
 */
public interface UserService {
    
    /**
     * 根据用户名和密码注册用户
     * @param username
     * @param password
     */
    public void insertUser(String username,String password);

    /**
     * 根据id返回用户信息
     * @param id
     * @return
     */
    public User selectById(String id);

    /**
     * 上传文件到数据库
     * @param file
     * @param user
     * @throws IOException 
     */
    public void insertAvatar(MultipartFile file,User user) throws IOException;
}

package com.ygh.service;

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
}

package com.ygh.service;

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
}

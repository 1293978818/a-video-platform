package com.ygh.domain;

import java.util.List;

import lombok.Data;

/**
 * 封装一定用户和其总数的类
 * @author ygh
 */
@Data
public class Users {
    
    private List<User> user;

    private Long total;
}

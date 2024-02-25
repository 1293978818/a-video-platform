package com.ygh.domain;

import java.util.List;

import lombok.Data;

/**
 * @author ygh
 */
@Data
public class Users {
    
    private List<User> user;

    private Long total;
}

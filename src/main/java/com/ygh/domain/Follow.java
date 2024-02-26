package com.ygh.domain;

import lombok.Data;

/**
 * 关注类
 * @author ygh
 */
@Data
public class Follow {
    
    private String userId;

    private String followedId;

    
}

package com.ygh.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * @author ygh
 */
@Data
@TableName("`like`")
public class Like {
    private String userId;

    private String videoId;

    private String commentId;
}

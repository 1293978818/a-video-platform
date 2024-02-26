package com.ygh.domain;

import java.util.List;

import lombok.Data;

/**
 * 封装视频和总数的类
 * @author ygh
 */
@Data
public class Videos{
    private List<Video> items;
    private Long total;
}

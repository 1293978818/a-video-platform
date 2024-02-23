package com.ygh.domain;

import java.util.List;

import lombok.Data;

/**
 * @author ygh
 */
@Data
public class Videos{
    private List<Video> items;
    private Long total;
}

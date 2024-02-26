package com.ygh.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结果的信息类
 * @author ygh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Base {
    private int code;
    private String msg;
}

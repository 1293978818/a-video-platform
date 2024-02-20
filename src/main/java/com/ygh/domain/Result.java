package com.ygh.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ygh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String accessToken;
    private String refreshToken;
    private Base base;
    private Object data;
}

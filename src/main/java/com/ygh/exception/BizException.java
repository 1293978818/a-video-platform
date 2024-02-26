package com.ygh.exception;

/**
 * 自定义异常
 * @author ygh
 */
public class BizException extends RuntimeException{
    
    public BizException(String msg){
        super(msg);
    }
}

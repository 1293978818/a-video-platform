package com.ygh.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ygh.domain.Base;
import com.ygh.domain.Result;
import com.ygh.exception.BizException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ygh
 */
@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    
    @ExceptionHandler(BizException.class)
    public Result doBizException(BizException bizException){
        Result result = new Result();
        result.setBase(new Base(-1, bizException.getMessage()));
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result unknownException(Exception exception){
        Result result = new Result();
        result.setBase(new Base(-1, exception.getMessage()));
        log.info(exception.getMessage());
        return result;
    }
}

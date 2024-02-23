package com.ygh.handler;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * @author ygh
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler{

    @Override
    public void insertFill(MetaObject metaObject) {
        Date date = new Date();
        this.setFieldValByName("createdAt", date, metaObject);
        this.setFieldValByName("updatedAt", date, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date date = new Date();
        this.setFieldValByName("updatedAt", date, metaObject);
    }
    
}

package com.ygh.handler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * 实现自动填充的处理器
 * @author ygh
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler{

    @Override
    public void insertFill(MetaObject metaObject) {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        Date date = Date.from(zdt.toInstant());
        this.setFieldValByName("createdAt", date, metaObject);
        this.setFieldValByName("updatedAt", date, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        Date date = Date.from(zdt.toInstant());
        this.setFieldValByName("updatedAt", date, metaObject);
    }
    
}

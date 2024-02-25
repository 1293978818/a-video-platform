package com.ygh.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygh.domain.Comment;

/**
 * @author ygh
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment>{
    
}

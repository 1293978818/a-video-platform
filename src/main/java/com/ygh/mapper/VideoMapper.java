package com.ygh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygh.domain.Video;

/**
 * 处理视频信息的持久层
 * @author ygh
 */
@Mapper
public interface VideoMapper extends BaseMapper<Video>{
    
    /**
     * 根据id删除视频
     * @param id
     */
    @Update("update video set deleted = 1,deleted_at = now() where id = #{id}")
    public void deleteById(Integer id);
}

package com.ygh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ygh.domain.Like;
import com.ygh.domain.Video;

/**
 * @author ygh
 */
@Mapper
public interface LikeMapper extends BaseMapper<Like>{
    
    /**
     * 查询用户点赞的视频
     * @param userId
     * @param page
     * @return
     */
    @Select("SELECT id,v.user_id,video_url,cover_url,title,description,visit_count,like_count,comment_count,created_at,updated_at,deleted_at,deleted,version FROM video AS v INNER JOIN `like` AS l ON v.id = l.video_id")
    public IPage<Video> selectLikedVideo(String userId, IPage<Video> page);
}

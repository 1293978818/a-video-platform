package com.ygh.service;

import java.util.List;

import com.ygh.domain.Video;

/**
 * 处理点赞业务的接口
 * @author ygh
 */
public interface LikeService {
    
    /**
     * 执行点赞操作
     * @param videoId
     * @param commentId
     * @param actionType
     * @param userId
     */
    public void action(String videoId,String commentId,String actionType,String userId);

    /**
     * 返回用户点赞的视频
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<Video> list(String userId, Integer pageSize, Integer pageNum);
}

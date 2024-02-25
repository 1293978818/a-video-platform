package com.ygh.service;

import java.util.List;

import com.ygh.domain.Comment;

/**
 * @author ygh
 */
public interface CommentService {
    
    /**
     * 发表评论
     * @param videoId
     * @param commentId
     * @param content
     * @param userId
     */
    public void publish(String videoId,String commentId,String content,String userId);

    /**
     * 查询评论
     * @param videoId
     * @param commentId
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<Comment> list(String videoId,String commentId,Integer pageSize,Integer pageNum);

    /**
     * 删除评论
     * @param commentId
     * @param userId
     */
    public void delete(String commentId,String userId);
}

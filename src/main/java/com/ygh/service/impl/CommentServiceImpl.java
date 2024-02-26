package com.ygh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ygh.domain.Comment;
import com.ygh.domain.Like;
import com.ygh.domain.Video;
import com.ygh.exception.BizException;
import com.ygh.mapper.CommentMapper;
import com.ygh.mapper.LikeMapper;
import com.ygh.mapper.VideoMapper;
import com.ygh.service.CommentService;

/**
 * 处理评论业务的实现类
 * @author ygh
 */
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public void publish(String videoId, String commentId, String content,String userId) {
        
        boolean flag = (videoId == null && commentId == null) || (videoId != null && commentId != null);
        if(flag){
            throw new BizException("视频id和评论id有且仅有一个");
        }

        String nums = "\\d+";
        if(commentId == null){

            if(!videoId.matches(nums)){
                throw new BizException("视频id非法");
            }

            Video video = videoMapper.selectById(videoId);

            if(video == null){
                throw new BizException("视频不存在");
            }

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUserId(userId);
            comment.setVideoId(videoId);
            commentMapper.insert(comment);

            video.setCommentCount(video.getCommentCount() + 1);
            videoMapper.updateById(video);
            return;
        }

        if(!commentId.matches(nums)){
            throw new BizException("评论id非法");
        }

        Comment comment = commentMapper.selectById(commentId);

        if(comment == null){
            throw new BizException("评论不存在");
        }

        Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setParentId(commentId);
        newComment.setUserId(userId);
        newComment.setVideoId(comment.getVideoId());

        commentMapper.insert(newComment);

        comment.setChildCount(comment.getChildCount() + 1);
        commentMapper.updateById(comment);

        Video video = videoMapper.selectById(comment.getVideoId());
        video.setCommentCount(video.getCommentCount() + 1);
        videoMapper.updateById(video);
    }

    @Override
    public List<Comment> list(String videoId, String commentId, Integer pageSize, Integer pageNum) {

        if(pageNum == null || pageSize == null){
            throw new BizException("页码信息不能为空");
        }

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }
        
        pageNum ++;
        
        boolean flag = (videoId == null && commentId == null) || (videoId != null && commentId != null);
        if(flag){
            throw new BizException("视频id和评论id有且仅有一个");
        }

        IPage<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String nums = "\\d+";
        if(commentId == null){

            if(!videoId.matches(nums)){
                throw new BizException("视频id非法");
            }

            Video video = videoMapper.selectById(videoId);

            if(video == null){
                throw new BizException("视频不存在");
            }

            lambdaQueryWrapper.eq(Comment::getVideoId, videoId)
                .eq(Comment::getParentId, -1);
            commentMapper.selectPage(page, lambdaQueryWrapper);
            return page.getRecords();
        }

        if(!commentId.matches(nums)){
            throw new BizException("评论id非法");
        }

        Comment comment = commentMapper.selectById(commentId);

        if(comment == null){
            throw new BizException("评论不存在");
        }

        lambdaQueryWrapper.eq(Comment::getId, commentId)
            .or()
            .eq(Comment::getParentId, commentId);
        commentMapper.selectPage(page, lambdaQueryWrapper);
        return page.getRecords();
    }

    @Override
    public void delete(String commentId, String userId) {
        
        if(commentId == null){
            throw new BizException("评论id为空");
        }

        Comment comment = commentMapper.selectById(commentId);

        if(comment == null){
            throw new BizException("评论不存在");
        }

        if(!comment.getUserId().equals(userId)){
            throw new BizException("没有权限删除此评论");
        }

        int count = delete(commentId);

        boolean flag = "-1".equals(comment.getParentId());
        if(!flag){
            Comment parentComment = commentMapper.selectById(comment.getParentId());
            parentComment.setChildCount(0);
            commentMapper.updateById(parentComment);
        }

        Video video = videoMapper.selectById(comment.getVideoId());
        video.setCommentCount(video.getCommentCount() - count);
        videoMapper.updateById(video);
    }

    public int delete(String commentId){

        int count = 0;

        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getParentId, commentId);
        List<Comment> list = commentMapper.selectList(lambdaQueryWrapper);

        for (Comment comment : list) {
            count += delete(comment.getId());
        }

        count ++;
        commentMapper.deleteById(commentId);
        
        LambdaQueryWrapper<Like> likeWrapper = new LambdaQueryWrapper<>();
        likeWrapper.eq(Like::getCommentId, commentId);
        likeMapper.delete(likeWrapper);
        
        return count;
    }
    
}

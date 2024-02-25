package com.ygh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ygh.domain.Comment;
import com.ygh.domain.Like;
import com.ygh.domain.User;
import com.ygh.domain.Video;
import com.ygh.exception.BizException;
import com.ygh.mapper.CommentMapper;
import com.ygh.mapper.LikeMapper;
import com.ygh.mapper.UserMapper;
import com.ygh.mapper.VideoMapper;
import com.ygh.service.LikeService;

/**
 * @author ygh
 */
@Service
public class LikeServiceImpl implements LikeService{

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void action(String videoId, String commentId, String actionType, String userId) {
        
        boolean flag = (videoId == null && commentId == null) || (videoId != null && commentId != null);
        if(flag){
            throw new BizException("视频id和评论id有且仅有一个");
        }

        flag = "1".equals(actionType);
        if(flag){
            like(videoId, commentId, userId);
            return;
        }

        flag = "2".equals(actionType);
        if(flag){
            dislike(videoId, commentId, userId);
            return;
        }

        throw new BizException("点赞操作不存在");
    }

    public void like(String videoId, String commentId, String userId){

        LambdaQueryWrapper<Like> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(videoId == null){

            Comment comment = commentMapper.selectById(commentId);

            if(comment == null){
                throw new BizException("评论不存在");
            }

            
            lambdaQueryWrapper.eq(Like::getUserId, userId).eq(Like::getCommentId, commentId);
            if(likeMapper.selectOne(lambdaQueryWrapper) != null){
                throw new BizException("已经点赞了");
            }

            Like like = new Like();
            like.setCommentId(commentId);
            like.setUserId(userId);

            likeMapper.insert(like);

            comment.setLikeCount(comment.getLikeCount() + 1);
            commentMapper.updateById(comment);
            return;
        }

        Video video = videoMapper.selectById(videoId);

        if(video == null){
            throw new BizException("视频不存在");
        }

        lambdaQueryWrapper.eq(Like::getUserId, userId).eq(Like::getVideoId, videoId);
        if(likeMapper.selectOne(lambdaQueryWrapper) != null){
            throw new BizException("已经点赞了");
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setVideoId(videoId);

        likeMapper.insert(like);

        video.setLikeCount(video.getLikeCount() + 1);
        videoMapper.updateById(video);
    }

    public void dislike(String videoId, String commentId, String userId){
        LambdaQueryWrapper<Like> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(videoId == null){

            Comment comment = commentMapper.selectById(commentId);

            if(comment == null){
                throw new BizException("评论不存在");
            }

            lambdaQueryWrapper.eq(Like::getUserId, userId).eq(Like::getCommentId, commentId);
            if(likeMapper.selectOne(lambdaQueryWrapper) == null){
                throw new BizException("未点赞");
            }

            likeMapper.delete(lambdaQueryWrapper);
            comment.setLikeCount(comment.getLikeCount() - 1);
            commentMapper.updateById(comment);
            return;
        }

        Video video = videoMapper.selectById(videoId);

        if(video == null){
            throw new BizException("视频不存在");
        }

        lambdaQueryWrapper.eq(Like::getUserId, userId).eq(Like::getVideoId, videoId);
        if(likeMapper.selectOne(lambdaQueryWrapper) == null){
            throw new BizException("未点赞");
        }

        likeMapper.delete(lambdaQueryWrapper);

        video.setLikeCount(video.getLikeCount() - 1);
        videoMapper.updateById(video);
    }

    @Override
    public List<Video> list(String userId, Integer pageSize, Integer pageNum) {

        if(pageNum == null || pageSize == null){
            throw new BizException("页码信息不能为空");
        }

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }

        pageNum ++;

        String nums = "\\d+";
        if(!userId.matches(nums)){
            throw new BizException("id非法");
        }
        
        User user = userMapper.selectById(userId);

        if(user == null){
            throw new BizException("用户不存在");
        }

        IPage<Video> page = new Page<>(pageNum,pageSize);
        likeMapper.selectLikedVideo(userId, page);

        return page.getRecords();
    }
    
}

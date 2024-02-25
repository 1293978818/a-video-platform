package com.ygh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygh.domain.Follow;
import com.ygh.domain.User;
import com.ygh.exception.BizException;
import com.ygh.mapper.FollowMapper;
import com.ygh.mapper.UserMapper;
import com.ygh.service.FollowService;

/**
 * @author ygh
 */
@Service
public class FollowServiceImpl implements FollowService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public void action(String userId, String toUserId, Integer actionType) {
        
        String nums = "\\d+";
        if(!toUserId.matches(nums)){
            throw new BizException("用户id非法");
        }

        if(userId.equals(toUserId)){
            throw new BizException("不能对自己进行操作");
        }

        if(actionType == 0){

            User user = userMapper.selectById(toUserId);
            if(user == null){
                throw new BizException("用户不存在");
            }

            LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Follow::getUserId, userId).eq(Follow::getFollowedId, toUserId);
            if(followMapper.selectOne(lambdaQueryWrapper) != null){
                throw new BizException("已经关注了");
            }

            Follow follow = new Follow();
            follow.setFollowedId(toUserId);
            follow.setUserId(userId);
            followMapper.insert(follow);

            return;
        }

        if(actionType == 1){
            User user = userMapper.selectById(toUserId);
            if(user == null){
                throw new BizException("用户不存在");
            }

            LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Follow::getUserId, userId).eq(Follow::getFollowedId, toUserId);
            if(followMapper.selectOne(lambdaQueryWrapper) == null){
                throw new BizException("未关注");
            }

            followMapper.delete(lambdaQueryWrapper);

            return;
        }

        throw new BizException("非法的操作");
    }
    
}

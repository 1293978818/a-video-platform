package com.ygh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ygh.domain.Follow;
import com.ygh.domain.User;
import com.ygh.domain.Users;
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

    @Override
    public Users followList(String userId, Integer pageNum, Integer pageSize) {

        if(pageNum == null || pageSize == null){
            throw new BizException("页码信息不能为空");
        }

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }

        pageNum ++;
        
        String nums = "\\d+";
        if(!userId.matches(nums)){
            throw new BizException("用户id非法");
        }

        User user = userMapper.selectById(userId);

        if(user == null){
            throw new BizException("用户不存在");
        }

        IPage<User> page = new Page<>(pageNum, pageSize);
        followMapper.selectFollowingList(userId, page);

        Users users = new Users();
        users.setUser(page.getRecords());
        users.setTotal(page.getTotal());

        return users;
    }

    @Override
    public Users followerList(String userId, Integer pageNum, Integer pageSize) {
        
        if(pageNum == null || pageSize == null){
            throw new BizException("页码信息不能为空");
        }

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }

        pageNum ++;
        
        String nums = "\\d+";
        if(!userId.matches(nums)){
            throw new BizException("用户id非法");
        }

        User user = userMapper.selectById(userId);

        if(user == null){
            throw new BizException("用户不存在");
        }

        IPage<User> page = new Page<>(pageNum, pageSize);
        followMapper.selectFollowerList(userId, page);

        Users users = new Users();
        users.setUser(page.getRecords());
        users.setTotal(page.getTotal());

        return users;
    }

    @Override
    public Users friendsList(String userId, Integer pageNum, Integer pageSize) {
        
        if(pageNum == null || pageSize == null){
            throw new BizException("页码信息不能为空");
        }

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }

        pageNum ++;

        IPage<User> page = new Page<>(pageNum, pageSize);
        followMapper.selectFriends(userId, page);

        Users users = new Users();
        users.setUser(page.getRecords());
        users.setTotal(page.getTotal());

        return users;
    }

    
}

package com.ygh.service;

import com.ygh.domain.Users;

/**
 * @author ygh
 */
public interface FollowService{
    
    /**
     * 执行关注操作
     * @param userId
     * @param toUserId
     * @param actionType
     */
    public void action(String userId,String toUserId,Integer actionType);

    /**
     * 查询关注列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Users followList(String userId,Integer pageNum,Integer pageSize);

    /**
     * 查看粉丝列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Users followerList(String userId,Integer pageNum,Integer pageSize);

    /**
     * 查询朋友列表
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Users friendsList(String userId,Integer pageNum,Integer pageSize);
}

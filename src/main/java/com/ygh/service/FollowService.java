package com.ygh.service;

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
}

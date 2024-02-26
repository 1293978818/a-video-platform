package com.ygh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ygh.domain.Follow;
import com.ygh.domain.User;

/**
 * 关注的持久层
 * @author ygh
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow>{
    
    /**
     * 查询关注的用户
     * @param userId
     * @param page
     * @return
     */
    @Select("SELECT id,username,avatar_url FROM user INNER JOIN follow ON id = followed_id where user_id = #{userId}")
    public IPage<User> selectFollowingList(String userId, IPage<User> page);

    /**
     * 查询粉丝
     * @param userId
     * @param page
     * @return
     */
    @Select("SELECT id,username,avatar_url FROM user INNER JOIN follow ON id = user_id where followed_id = #{userId}")
    public IPage<User> selectFollowerList(String userId, IPage<User> page);

    /**
     * 查询朋友
     * @param userId
     * @param page
     * @return
     */
    @Select("SELECT id,username,avatar_url FROM follow f1 INNER JOIN follow f2 ON f1.user_id = f2.followed_id AND f1.followed_id = f2.user_id INNER JOIN user u ON f1.user_id = u.id WHERE f1.followed_id = #{userId};")
    public IPage<User> selectFriends(String userId, IPage<User> page);

}

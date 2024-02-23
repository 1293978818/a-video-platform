package com.ygh.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygh.domain.User;

/**
 * @author ygh
 */
@Mapper
public interface UserMapper extends BaseMapper<User>{
    
    /**
     * 根据用户名删除用户
     * @param username
     */
    @Update("update user set deleted = 1,deleted_at = now() where username = #{username}")
    public void deleteByUsername(String username);

    /**
     * 根据用户名上传头像
     * @param avatarUrl
     * @param username
     */
    @Update("update user set avatar_url = #{avatarUrl} where username = #{username}")
    public void insertAvatar(String avatarUrl,String username);
}

package com.ygh.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ygh.domain.User;
import com.ygh.domain.Videos;

/**
 * 处理视频业务的接口
 * @author ygh
 */
public interface VideoService {
    
    /**
     * 投稿视频
     * @param file
     * @param title
     * @param description
     * @param user
     * @throws IOException 
     */
    public void publish(MultipartFile file,String title,String description,User user) throws IOException;

    /**
     * 根据id返回视频
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Videos list(String userId,Integer pageNum,Integer pageSize); 

    /**
     * 返回热门排行榜的内容
     * @param pageSize
     * @param pageNum
     * @return
     */
    public Videos popular(Integer pageSize,Integer pageNum);

    /**
     * 根据一定条件搜索视频
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @param fromDate
     * @param toDate
     * @param username
     * @return
     */
    public Videos search(String keyword, Integer pageSize, Integer pageNum, Long fromDate, Long toDate, String username);
}

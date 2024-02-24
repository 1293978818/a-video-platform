package com.ygh.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ygh.domain.User;
import com.ygh.domain.Video;
import com.ygh.domain.Videos;
import com.ygh.exception.BizException;
import com.ygh.mapper.UserMapper;
import com.ygh.mapper.VideoMapper;
import com.ygh.service.VideoService;

import cn.hutool.core.lang.Snowflake;

/**
 * @author ygh
 */
@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void publish(MultipartFile file, String title, String description, User user) throws IOException {

        if(file == null){
            throw new BizException("视频不能为空");
        }

        String contentType = file.getContentType();
        boolean flag = contentType == null || (!contentType.startsWith("video"));
        if(flag){
            throw new BizException("视频格式错误");
        }

        if(title == null){
            title = "Untitled";
        }

        if(description == null){
            description = "There are no description";
        }

        long id = snowflake.nextId();

        byte[] bytes = file.getBytes();
        String url = "D:\\code\\javalearning\\work4\\videos\\" 
            + id + "." + contentType.split("/")[1];
        Path path = Paths.get(url);
        Files.write(path, bytes);

        Video video = new Video();
        video.setId(Long.toString(id));
        video.setUserId(user.getId());
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(url);

        videoMapper.insert(video);

        stringRedisTemplate.opsForZSet().add("visit_count_ranking", Long.toString(id), 0);
    }

    @Override
    public Videos list(String userId, Integer pageNum, Integer pageSize) {

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }

        pageNum ++;
        
        if(userId == null){
            throw new BizException("id不能为空");
        }

        String nums = "\\d+";
        if(!userId.matches(nums)){
            throw new BizException("id非法");
        }

        User user = userMapper.selectById(userId);
        if(user == null){
            throw new BizException("userid不存在");
        }

        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Video::getUserId,userId);

        IPage<Video> iPage = new Page<>(pageNum,pageSize);
        videoMapper.selectPage(iPage, lambdaQueryWrapper);

        Videos videos = new Videos();
        videos.setItems(iPage.getRecords());
        videos.setTotal(iPage.getTotal());

        return videos;
    }

    @Override
    public Videos popular(Integer pageSize, Integer pageNum) {

        if(pageNum == null || pageSize == null){
            throw new BizException("页码信息不能为空");
        }

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }

        int start = pageNum * pageSize;
        int end = (pageNum + 1) * pageSize - 1;
        Long size = stringRedisTemplate.opsForZSet().size("visit_count_ranking");
        if (end > size) {
            end = -1;
        }

        Set<String> idSet = stringRedisTemplate.opsForZSet().reverseRange("visit_count_ranking", start, end);
        List<Video> popularVideo = new ArrayList<>();

        for(String id : idSet){
            Video video = videoMapper.selectById(id);
            popularVideo.add(video);
        }

        Videos videos = new Videos();
        videos.setItems(popularVideo);

        return videos;
    }

    @Override
    public Videos search(String keyword, Integer pageSize, Integer pageNum, Long fromDate, Long toDate, String username) {
        pageNum ++;

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息不合法");
        }

        Videos videos = new Videos();
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(!"".equals(keyword)){
            lambdaQueryWrapper.and(i -> {
                i.like(Video::getTitle, keyword).or().like(Video::getDescription, keyword);
            });
        }

        if(fromDate != null && fromDate > 0){
            String fDate = new Timestamp(fromDate).toString();
            lambdaQueryWrapper.gt(Video::getCreatedAt, fDate);
        }

        if(toDate != null && toDate > 0){
            String tDate = new Timestamp(toDate).toString();
            lambdaQueryWrapper.lt(Video::getCreatedAt, tDate);
        }

        IPage<Video> page = new Page<>(pageNum, pageSize);
        if(username == null){
            videoMapper.selectPage(page, lambdaQueryWrapper);
            videos.setItems(page.getRecords());
            videos.setTotal(page.getTotal());
            return videos;
        }

        LambdaQueryWrapper<User> userWapper = new LambdaQueryWrapper<>();
        userWapper.like(User::getUsername, username);
        List<User> users = userMapper.selectList(userWapper);

        lambdaQueryWrapper.and(i -> {
            for (User user : users) {
                i.or().eq(Video::getUserId,user.getId());
            }
        });

        videoMapper.selectPage(page, lambdaQueryWrapper);
        videos.setItems(page.getRecords());
        videos.setTotal(page.getTotal());
        return videos;
    }
    
}

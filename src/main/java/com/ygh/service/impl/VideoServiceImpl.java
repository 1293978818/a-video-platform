package com.ygh.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import com.ygh.service.VideoLoadService;
import com.ygh.service.VideoService;

import cn.hutool.core.lang.Snowflake;

/**
 * 处理视频业务的实现类
 * @author ygh
 */
@Service
public class VideoServiceImpl implements VideoService{

    @Value("${my.video_address}")
    private String baseUrl;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private VideoLoadService videoLoadService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publish(MultipartFile file, String title, String description, User user) throws IOException, InterruptedException, ExecutionException {

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

        String url = baseUrl
            + id + "." + contentType.split("/")[1];
        CompletableFuture<Boolean> load = videoLoadService.loadVideo(url, file);

        Video video = new Video();
        video.setId(Long.toString(id));
        video.setUserId(user.getId());
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(url);

        videoMapper.insert(video);

        stringRedisTemplate.opsForZSet().add("visit_count_ranking", Long.toString(id), 0);

        Boolean success = load.get();

        if(success){
            return;
        }else{
            videoMapper.deleteForever(id);
            throw new BizException("视频上传失败，请重试");
        }
    }

    @Override
    public Videos list(String userId, Integer pageNum, Integer pageSize) {

        isPageRight(pageNum, pageSize);

        pageNum ++;
        
        isIdRight(userId);

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

        isPageRight(pageNum, pageSize);

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
    public Videos search(String keyword, Integer pageSize, Integer pageNum, Long fromDate, Long toDate, String username, String userId, Integer mode, Integer column) {

        isPageRight(pageNum, pageSize);

        pageNum ++;

        Videos videos = new Videos();
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if(!"".equals(keyword)){
            lambdaQueryWrapper.and(i -> {
                i.like(Video::getTitle, keyword).or().like(Video::getDescription, keyword);
            });
            
            if(userId != null){
                stringRedisTemplate.opsForList().leftPush("search_history:" + userId, keyword);
            }
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

            orderByCondition(mode, column, lambdaQueryWrapper);
            
            videoMapper.selectPage(page, lambdaQueryWrapper);
            videos.setItems(page.getRecords());
            videos.setTotal(page.getTotal());
            return videos;
        }

        if(userId != null){
            stringRedisTemplate.opsForList().leftPush("search_history:" + userId, username);
        }

        LambdaQueryWrapper<User> userWapper = new LambdaQueryWrapper<>();
        userWapper.like(User::getUsername, username);
        List<User> users = userMapper.selectList(userWapper);
        List<String> ids = new ArrayList<>();

        for (User user : users) {
            ids.add(user.getId());
        }

        if(!ids.isEmpty()){
            lambdaQueryWrapper.in(Video::getUserId, ids);
        }

        orderByCondition(mode, column, lambdaQueryWrapper);

        videoMapper.selectPage(page, lambdaQueryWrapper);
        videos.setItems(page.getRecords());
        videos.setTotal(page.getTotal());
        return videos;
    }

    

    @Override
    public List<String> searchHistory(String userId, Integer pageNum, Integer pageSize) {
        
        isPageRight(pageNum, pageSize);

        isIdRight(userId);

        int start = pageNum * pageSize;
        int end = (pageNum + 1) * pageSize - 1;
        List<String> histories = stringRedisTemplate.opsForList().range("search_history:" + userId, start, end);
        return histories;
    }

    private void isPageRight(Integer pageNum, Integer pageSize){

        if(pageNum == null || pageSize == null){
            throw new BizException("页码信息不能为空");
        }

        if(pageNum < 0 || pageSize < 0){
            throw new BizException("页码信息非法");
        }
    }
    
    private void isIdRight(String id){

        if(id == null){
            throw new BizException("id不能为空");
        }

        String nums = "\\d+";
        if(!id.matches(nums)){
            throw new BizException("id非法");
        }
    }

    private void orderByCondition(Integer mode, Integer column, LambdaQueryWrapper<Video> lambdaQueryWrapper){

        
        switch (column) {
            case 0:
                lambdaQueryWrapper.orderBy(mode == 0, mode == 0, Video::getVisitCount);
                lambdaQueryWrapper.orderBy(mode == 1, mode != 1, Video::getVisitCount);
                break;
            case 1:
                lambdaQueryWrapper.orderBy(mode == 0, mode == 0, Video::getCreatedAt);
                lambdaQueryWrapper.orderBy(mode == 1, mode != 1, Video::getCreatedAt);
                break;
            default:
                break;
        }
    }
}

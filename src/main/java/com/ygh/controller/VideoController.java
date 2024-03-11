package com.ygh.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygh.domain.Base;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.domain.Videos;
import com.ygh.service.VideoService;
import com.ygh.util.JwtUtil;

/**
 * 用于视频操作的控制器
 * @author ygh
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/publish")
    public Result publish(@RequestParam(value = "data",required = false) MultipartFile file,
    @RequestParam(value = "title", required = false) String title,
    @RequestParam(value = "description", required = false) String description,
    @RequestHeader("Access-Token") String accessToken) throws IOException{
        Result result = new Result();
        Base base = new Base();
        
        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        videoService.publish(file, title, description, user);

        base.setCode(10000);
        base.setMsg("success");

        result.setBase(base);
        
        return result;
    }

    @GetMapping("/list")
    public Result list(@RequestParam("user_id") String userId,
    @RequestParam("page_num") Integer pageNum,
    @RequestParam("page_size") Integer pageSize){
        Result result = new Result();
        Base base = new Base();

        Videos videos = videoService.list(userId, pageNum, pageSize);

        result.setData(videos);
        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);

        return result;
    }

    @GetMapping("/popular")
    public Result popular(@RequestParam(value = "page_size", required = false) Integer pageSize,
    @RequestParam(value = "page_num",required = false) Integer pageNum){
        Result result = new Result();
        Base base = new Base();

        Videos videos = videoService.popular(pageSize, pageNum);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(videos);
        return result;
    }

    @PostMapping("/search")
    public Result search(@RequestParam("keywords") String keywords,
    @RequestParam("page_size") Integer pageSize,
    @RequestParam("page_num") Integer pageNum,
    @RequestParam(value = "from_date", required = false) Long fromDate,
    @RequestParam(value = "to_date", required = false) Long toDate,
    @RequestParam(value = "username", required = false) String username,
    @RequestParam(value = "mode", required = false) Integer mode,
    @RequestParam(value = "column", required = false) Integer column,
    @RequestHeader(value = "Access-Token", required = false) String accessToken) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String userId = null;
        if(accessToken != null){
            String userInfo = jwtUtil.getUserInfo(accessToken);
            User user = objectMapper.readValue(userInfo, User.class);
            userId = user.getId();
        }

        Videos videos = videoService.search(keywords, pageSize, pageNum, fromDate, toDate, username, userId, mode, column);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(videos);
        return result;
    }

    @GetMapping("/search/history")
    public Result searchHistory(@RequestParam("page_size") Integer pageSize,
    @RequestParam("page_num") Integer pageNum,
    @RequestHeader("Access-Token") String accessToken) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        result.setData(videoService.searchHistory(user.getId(), pageNum, pageSize));
        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }


}

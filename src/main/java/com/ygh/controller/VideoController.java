package com.ygh.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygh.domain.Base;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.domain.Videos;
import com.ygh.service.VideoService;
import com.ygh.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

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
    HttpServletRequest request) throws IOException{
        Result result = new Result();
        Base base = new Base();
        
        String accessToken = request.getHeader("Access-Token");
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
    @RequestParam(value = "username", required = false) String username){
        Result result = new Result();
        Base base = new Base();

        Videos videos = videoService.search(keywords, pageSize, pageNum, fromDate, toDate, username);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(videos);
        return result;
    }
}

package com.ygh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygh.domain.Base;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.domain.Video;
import com.ygh.service.LikeService;
import com.ygh.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author ygh
 */
@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/action")
    public Result action(@RequestParam(value = "video_id", required = false) String videoId,
    @RequestParam(value = "comment_id", required = false) String commentId,
    @RequestParam(value = "action_type", required = false) String actionType,
    HttpServletRequest request) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String accessToken = request.getHeader("Access-Token");
        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        likeService.action(videoId, commentId, actionType, user.getId());

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }

    @GetMapping("/list")
    public Result list(@RequestParam(value = "user_id", required = false) String userId,
    @RequestParam(value = "page_size", required = false) Integer pageSize,
    @RequestParam(value = "page_num", required = false) Integer pageNum){
        Result result = new Result();
        Base base = new Base();

        List<Video> list = likeService.list(userId, pageSize, pageNum);

        result.setData(list);
        base.setCode(10000);
        base.setMsg("success");
        return result;
    }
}

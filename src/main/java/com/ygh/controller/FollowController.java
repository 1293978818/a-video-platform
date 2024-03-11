package com.ygh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygh.domain.Base;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.domain.Users;
import com.ygh.service.FollowService;
import com.ygh.util.JwtUtil;

/**
 * 用于关注操作的控制器
 * @author ygh
 */
@RestController
@RequestMapping
public class FollowController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FollowService followService;
    
    @PostMapping("/relation/action")
    public Result action(@RequestParam("to_user_id") String toUserId,
    @RequestParam("action_type") Integer actionType,
    @RequestHeader("Access-Token") String accessToken) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        followService.action(user.getId(), toUserId, actionType);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }

    @GetMapping("/following/list")
    public Result followingList(@RequestParam("user_id") String userId,
    @RequestParam(value = "page_size", required = false) Integer pageSize,
    @RequestParam(value = "page_num", required = false) Integer pageNum){
        Result result = new Result();
        Base base = new Base();

        Users users = followService.followList(userId, pageNum, pageSize);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(users);
        return result;
    }

    @GetMapping("/follower/list")
    public Result followerList(@RequestParam("user_id") String userId,
    @RequestParam(value = "page_size", required = false) Integer pageSize,
    @RequestParam(value = "page_num", required = false) Integer pageNum){
        Result result = new Result();
        Base base = new Base();

        Users users = followService.followerList(userId, pageNum, pageSize);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(users);
        return result;
    }

    @GetMapping("/friends/list")
    public Result friendsList(@RequestParam(value = "page_size", required = false) Integer pageSize,
    @RequestParam(value = "page_num", required = false) Integer pageNum,
    @RequestHeader("Access-Token") String accessToken) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        Users users = followService.followerList(user.getId(), pageNum, pageSize);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(users);
        return result;
    }
}

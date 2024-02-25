package com.ygh.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ygh.service.FollowService;
import com.ygh.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
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
    HttpServletRequest request) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String accessToken = request.getHeader("Access-Token");
        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        followService.action(user.getId(), toUserId, actionType);

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }
}

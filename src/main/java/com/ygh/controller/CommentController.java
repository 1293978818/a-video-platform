package com.ygh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ygh.domain.Comment;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.service.CommentService;
import com.ygh.util.JwtUtil;

/**
 * 处理评论信息的控制器
 * @author ygh
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;
    
    @PostMapping("/publish")
    public Result publish(@RequestParam(value = "video_id", required = false) String videoId,
    @RequestParam(value = "comment_id",required = false) String commentId,
    @RequestParam(value = "content") String content,
    @RequestHeader("Access-Token") String accessToken) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        commentService.publish(videoId, commentId, content, user.getId());

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }

    @GetMapping("/list")
    public Result list(@RequestParam(value = "video_id", required = false) String videoId,
    @RequestParam(value = "comment_id", required = false) String commentId,
    @RequestParam(value = "page_size", required = false) Integer pageSize,
    @RequestParam(value = "page_num",required = false) Integer pageNum){
        Result result = new Result();
        Base base = new Base();

        List<Comment> list = commentService.list(videoId, commentId, pageSize, pageNum);

        result.setData(list);
        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam(value = "comment_id", required = false) String commentId,
    @RequestHeader("Access-Token") String accessToken) throws JsonMappingException, JsonProcessingException{
        Result result = new Result();
        Base base = new Base();

        String userInfo = jwtUtil.getUserInfo(accessToken);
        User user = objectMapper.readValue(userInfo, User.class);

        commentService.delete(commentId, user.getId());

        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }
}

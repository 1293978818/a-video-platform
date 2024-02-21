package com.ygh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ygh.domain.Base;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.service.UserService;

/**
 * @author ygh
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @PostMapping("/register")
    public Result register(@RequestParam("username") String username,@RequestParam("password") String password){
        Result result = new Result();
        userService.insertUser(username,password);
        Base base = new Base();
        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        return result;
    }

    @GetMapping("/info")
    public Result info(@RequestParam(value = "user_id",required = false) String id){
        Result result = new Result();
        User user = userService.selectById(id);
        Base base = new Base();
        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(user);
        return result;
    }

}

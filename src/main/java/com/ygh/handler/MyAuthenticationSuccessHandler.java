package com.ygh.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygh.domain.Base;
import com.ygh.domain.MyUserDetails;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 认证成功处理器
 * @author ygh
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;
   
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Result result = new Result();
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = myUserDetails.getUser();
        String userInfo = objectMapper.writeValueAsString(user);
        String accessToken = jwtUtil.createJwt(userInfo, JwtUtil.TOKEN_ACCESS);
        String refreshToken = jwtUtil.createJwt(userInfo, JwtUtil.TOKEN_REFRESH);
        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        Base base = new Base();
        base.setCode(10000);
        base.setMsg("success");
        result.setBase(base);
        result.setData(user);
        print(response, result);
        
    }

    private void print(HttpServletResponse response,Result result) throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(result));
        writer.flush();
    }

}

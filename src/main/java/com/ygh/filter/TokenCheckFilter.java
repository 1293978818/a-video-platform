package com.ygh.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygh.domain.Base;
import com.ygh.domain.Result;
import com.ygh.domain.User;
import com.ygh.util.JwtUtil;

import io.netty.util.internal.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author ygh
 */
@Component
public class TokenCheckFilter extends OncePerRequestFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Result result = new Result();
        Base base = new Base();

        List<String> whiteList = new ArrayList<>();
        whiteList.add("/user/login");
        whiteList.add("/user/register");
        whiteList.add("/user/info");
        whiteList.add("/video/list");
        whiteList.add("/video/popular");
        whiteList.add("/video/search");

        if(whiteList.contains(request.getRequestURI())){
            doFilter(request, response, filterChain);
            return;
        }

        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");

        if(StringUtil.isNullOrEmpty(refreshToken) || StringUtil.isNullOrEmpty(accessToken)){
            base.setCode(-1);
            base.setMsg("用户未登录");
            result.setBase(base);
            print(response, result);
            return;
        }

        String userInfo = "";

        if(jwtUtil.verifyJwt(accessToken)){
            userInfo = jwtUtil.getUserInfo(accessToken);
            response.setHeader("Access-Token", accessToken);
            response.setHeader("Refresh-Token", refreshToken);
            setSecurityContext(userInfo, response);
            doFilter(request, response, filterChain);
            return;
        }

        if(jwtUtil.verifyJwt(refreshToken)){
            userInfo = jwtUtil.getUserInfo(refreshToken);
            accessToken = jwtUtil.createJwt(userInfo, JwtUtil.TOKEN_ACCESS);
            refreshToken = jwtUtil.createJwt(userInfo, JwtUtil.TOKEN_REFRESH);
            response.setHeader("Access-Token", accessToken);
            response.setHeader("Refresh-Token", refreshToken);
            setSecurityContext(userInfo, response);
            doFilter(request, response, filterChain);
            return;
        }

        base.setCode(-1);
        base.setMsg("登录认证已失效");
        result.setBase(base);
        print(response, result);
    }

    private void setSecurityContext(String userInfo,HttpServletResponse response) throws JsonMappingException, JsonProcessingException{
        User user = objectMapper.readValue(userInfo, User.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null,null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private void print(HttpServletResponse response,Result result) throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(result));
        writer.flush();
    }
    
}

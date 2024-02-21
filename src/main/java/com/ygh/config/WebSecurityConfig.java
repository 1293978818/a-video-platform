package com.ygh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ygh.filter.TokenCheckFilter;
import com.ygh.handler.MyAuthenticationFailureHandler;
import com.ygh.handler.MyAuthenticationSuccessHandler;

/**
 * @author ygh
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private TokenCheckFilter tokenCheckFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.addFilterBefore(tokenCheckFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> {
            auth.requestMatchers("/user/register").permitAll()
                .anyRequest().authenticated();
        })
        .formLogin(login -> {
            login.loginProcessingUrl("/user/login").permitAll()
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler);
        })
        .sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        })
        .csrf(csrf -> {
            csrf.disable();
        });
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

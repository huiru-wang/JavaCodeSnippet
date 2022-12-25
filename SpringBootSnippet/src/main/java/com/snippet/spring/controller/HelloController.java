package com.snippet.spring.controller;

import com.snippet.spring.dao.UserMapper;
import com.snippet.spring.dao.entity.User;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/hello")
    public BaseResponse<String> hello() {
        return ResponseUtil.success("hello");
    }

    @GetMapping("/user/info")
    public BaseResponse<User> getUserInfo(@RequestParam String userName) {
        User user = userMapper.selectByUserName(userName);
        return ResponseUtil.success(user);
    }
}

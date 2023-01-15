package com.snippet.spring.controller;

import com.snippet.spring.dao.entity.User;
import com.snippet.spring.dao.mapper.UserMapper;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/info")
    public BaseResponse<User> getUserInfo(@RequestParam String userName) {
        User user = userMapper.selectByUserName(userName);
        user.setPassword(null);
        return ResponseUtil.success(user);
    }
}

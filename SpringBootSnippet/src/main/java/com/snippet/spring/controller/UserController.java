package com.snippet.spring.controller;

import cn.hutool.core.lang.Assert;
import com.snippet.spring.dao.entity.User;
import com.snippet.spring.dao.mapper.UserMapper;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/info")
    public BaseResponse<User> getUserInfo(HttpServletRequest request) {
        String username = request.getParameter("username");
        Assert.notBlank(username, "username is blank");
        User user = userMapper.selectByUserName(username);
        user.setPassword(null);
        return ResponseUtil.success(user);
    }
}

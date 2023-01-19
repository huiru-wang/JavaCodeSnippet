package com.snippet.spring.service;

import cn.hutool.core.lang.Assert;
import com.snippet.spring.dao.entity.User;
import com.snippet.spring.dao.mapper.UserMapper;
import com.snippet.spring.model.request.UpdateUserInfoRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userService")
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserByUserName(@NonNull String username) {
        Assert.notBlank(username, "username is blank");
        User user = userMapper.selectByUserName(username);
        user.setPassword(null);
        return user;
    }

    public User updateUserInfo(UpdateUserInfoRequest request) {
        return null;
    }
}

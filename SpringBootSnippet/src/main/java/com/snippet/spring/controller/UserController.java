package com.snippet.spring.controller;

import com.snippet.spring.dao.entity.User;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.model.request.UpdateUserInfoRequest;
import com.snippet.spring.service.UserService;
import com.snippet.spring.util.ResponseUtil;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public BaseResponse<User> getUserInfo(@RequestParam @NonNull String username) {
        User user = userService.getUserByUserName(username);
        return ResponseUtil.success(user);
    }

    @PostMapping("/update")
    public BaseResponse<User> editUserInfo(@RequestBody @Validated UpdateUserInfoRequest request) {
        User user = userService.updateUserInfo(request);
        return ResponseUtil.success(user);
    }
}

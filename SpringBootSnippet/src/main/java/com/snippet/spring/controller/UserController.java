package com.snippet.spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snippet.spring.aop.annotation.AccessToken;
import com.snippet.spring.common.enums.ResponseEnums;
import com.snippet.spring.dao.entity.User;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.model.request.UpdateUserInfoRequest;
import com.snippet.spring.service.UserService;
import com.snippet.spring.util.ResponseUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:40:02
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public BaseResponse<User> getUserInfo(@RequestParam @NonNull String username) {
        User queryParam = new User();
        queryParam.setUsername(username);
        User user = userService.getOne(new QueryWrapper<>(queryParam));
        user.setPassword(null);
        return ResponseUtils.ok(user);
    }

    @AccessToken
    @PostMapping("/update")
    public BaseResponse<Object> updateUserInfo(@RequestBody @Validated UpdateUserInfoRequest request) {
        User queryParam = new User();
        queryParam.setId(request.getUserId());
        boolean res = userService.updateById(queryParam);
        return res ? ResponseUtils.ok() : ResponseUtils.fail(ResponseEnums.USER_INFO_UPDATE_FAIL);
    }
}

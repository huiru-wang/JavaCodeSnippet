package com.snippet.spring.controller;

import com.snippet.spring.cache.HelloCacheManager;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.model.UserCache;
import com.snippet.spring.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/api/hello")
public class HelloController {


    @GetMapping("")
    public BaseResponse<String> hello() {
        return ResponseUtils.ok("hello");
    }

    @Autowired
    HelloCacheManager helloCacheManager;

    @GetMapping("/valid")
    public BaseResponse<UserCache> helloCache(@Valid @NotBlank @RequestParam String username) {
        return ResponseUtils.ok(helloCacheManager.cache(username));
    }

    @GetMapping("/invalidCache")
    public BaseResponse<UserCache> helloInvalidCache(@Valid @NotBlank @RequestParam String username) {
        helloCacheManager.invalidCache(username);
        return ResponseUtils.ok();
    }
}

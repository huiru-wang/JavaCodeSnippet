package com.snippet.spring.controller;

import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.model.UserCache;
import com.snippet.spring.service.HelloService;
import com.snippet.spring.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping("get")
    public BaseResponse<String> hello() {
        return ResponseUtils.ok("hello");
    }

    @PostMapping("post")
    public BaseResponse<String> helloPost() {
        return ResponseUtils.ok("hello");
    }

    @Autowired
    HelloService helloService;

    @GetMapping("/valid")
    public BaseResponse<UserCache> helloCache(@Valid @NotBlank @RequestParam String username) {
        return ResponseUtils.ok(helloService.cache(username));
    }

    @GetMapping("/invalidCache")
    public BaseResponse<UserCache> helloInvalidCache(@Valid @NotBlank @RequestParam String username) {
        helloService.invalidCache(username);
        return ResponseUtils.ok();
    }

    @GetMapping("retry")
    public ResponseEntity<String> helloRetry() {
        return helloService.retryBadRequest();
    }

    @GetMapping("badResponse")
    public ResponseEntity<String> helloBadResponse() {
        return helloService.badRequest();
    }
}

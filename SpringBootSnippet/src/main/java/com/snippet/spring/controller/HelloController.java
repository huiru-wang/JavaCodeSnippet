package com.snippet.spring.controller;

import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/hello")
public class HelloController {


    @GetMapping("")
    public BaseResponse<String> hello() {
        return ResponseUtil.success("hello");
    }
    
    @Autowired
    HelloCacheManager helloCacheManager;

    @GetMapping("/valid")
    public BaseResponse<UserCache> helloCache(@Valid @NotBlank @RequestParam String username) {
        return ResponseUtils.success(helloCacheManager.cache(username));
    }

    @GetMapping("/invalidCache")
    public BaseResponse<UserCache> helloInvalidCache(@Valid @NotBlank @RequestParam String username) {
        helloCacheManager.invalidCache(username);
        return ResponseUtils.success();
    }
}

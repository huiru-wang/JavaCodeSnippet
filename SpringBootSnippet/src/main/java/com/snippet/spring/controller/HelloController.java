package com.snippet.spring.controller;

import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class HelloController {


    @GetMapping("/hello")
    public BaseResponse<String> hello() {
        return ResponseUtil.success("hello");
    }


}

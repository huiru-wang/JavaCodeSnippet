package com.snippet.httpclientimpl.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by whr on 2023/2/19
 */
@Slf4j
@RequestMapping("/api")
@RestController
public class HelloController {

    @GetMapping("get")
    public ResponseEntity<String> helloGet() {
        return ResponseEntity.status(200).body("get response");
    }

    @PostMapping("post")
    public ResponseEntity<String> helloPost() {
        return ResponseEntity.status(200).body("post response");
    }

    private int maxTimes = 2;

    @PostMapping("retry")
    public ResponseEntity<String> helloRetry() {
        if (maxTimes > 0) {
            maxTimes--;
            return ResponseEntity.status(500).body("Internal Error");
        }
        maxTimes = 2;
        return ResponseEntity.status(200).body("retry response");
    }
}

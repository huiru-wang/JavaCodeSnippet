package com.snippet.localcache.controller;

import com.snippet.localcache.model.UserInfo;
import com.snippet.localcache.model.UserInfoQueryModel;
import com.snippet.localcache.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * create by whr on 2023/2/19
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    HelloService helloService;

    @GetMapping("/users/list")
    public ResponseEntity<List<UserInfo>> listUserInfo(@RequestParam String username, @RequestParam String addr) {
        UserInfoQueryModel userInfoQueryModel = new UserInfoQueryModel(username, addr);
        List<UserInfo> userInfos = helloService.listUserInfo(userInfoQueryModel);
        return ResponseEntity.status(200).body(userInfos);
    }

    @GetMapping("/users/update")
    public ResponseEntity<String> updateUserInfo() {
        helloService.updateUserInfo();
        //helloService.addUserInfo();
        //helloService.deleteUserInfo();
        return ResponseEntity.ok("ok");
    }
}

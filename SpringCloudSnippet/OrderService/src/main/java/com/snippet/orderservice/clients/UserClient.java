package com.snippet.orderservice.clients;


import com.snippet.orderservice.dao.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") String id);
}

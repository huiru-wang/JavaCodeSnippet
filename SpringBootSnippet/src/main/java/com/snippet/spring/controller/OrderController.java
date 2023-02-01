package com.snippet.spring.controller;

import com.snippet.spring.aop.annotation.AccessToken;
import com.snippet.spring.dao.entity.Order;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.service.OrderService;
import com.snippet.spring.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @AccessToken
    @GetMapping("/export")
    public BaseResponse<Order> export(HttpServletRequest request,
                                      HttpServletResponse httpServletResponse) {

        return ResponseUtils.ok();
    }
}

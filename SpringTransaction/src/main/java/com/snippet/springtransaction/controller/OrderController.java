package com.snippet.springtransaction.controller;

import com.snippet.springtransaction.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by whr on 2023/3/3
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/t1")
    public String transactionTest1() {
        orderService.annotationTransaction();
        return "success";
    }

    @GetMapping("/t2")
    public String transactionTest2() {
        orderService.timeoutTransaction1();
        return "success";
    }

    @GetMapping("/t3")
    public String transactionTest3() {
        orderService.timeoutTransaction2();
        return "success";
    }

    @GetMapping("/t4")
    public String transactionTest4() {
        orderService.codeTransaction();
        return "success";
    }

    @GetMapping("/fail1")
    public String transactionTest5() {
        orderService.failCondition1();
        return "success";
    }
}

package com.snippet.springtransaction;

import com.snippet.springtransaction.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * create by whr on 2023/3/3
 */
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    public void timeout_test_fail() {
        orderService.timeoutTransaction1();
    }

    @Test
    public void timeout_test_success() {
        orderService.timeoutTransaction2();
    }

    @Test
    public void template_test() {
        orderService.templateTest();
    }
}
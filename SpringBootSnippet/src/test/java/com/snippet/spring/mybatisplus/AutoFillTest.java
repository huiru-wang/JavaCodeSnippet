package com.snippet.spring.mybatisplus;

import com.snippet.spring.dao.entity.Order;
import com.snippet.spring.dao.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * create by whr on 2023/2/15
 */
@SpringBootTest
public class AutoFillTest {

    @Autowired
    private OrderMapper orderMapper;

    public Order createOrder() {
        Order order = new Order();
        order.setUserId(1L);
        order.setAmount(1111L);
        order.setChannel("Adyen");
        order.setStatus(0);
        return order;
    }

    @Test
    public void auto_fill_by_custom_sql_test() {
        Order order = createOrder();
        orderMapper.insertOrder(order);
    }

    @Test
    public void auto_fill_by_baseMapper_test() {
        Order order = createOrder();
        orderMapper.insert(order);
    }
}

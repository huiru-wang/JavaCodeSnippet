package com.snippet.spring.mybatisplus;

import cn.hutool.core.util.IdUtil;
import com.snippet.spring.dao.entity.Order;
import com.snippet.spring.dao.mapper.OrderMapper;
import com.snippet.spring.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Transactional(rollbackFor = Exception.class) // 测试下默认自动回滚
 * create by whr on 2023/2/15
 */
@SpringBootTest
public class InsertTest {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderService orderService;


    public List<Order> createOrders() {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            Order order = new Order();
            order.setId(IdUtil.getSnowflakeNextId());
            order.setAmount(666_666L);
            order.setChannel("Adyen");
            order.setStatus(1);
            order.setUserId(IdUtil.getSnowflakeNextId());
            order.setRemarks("remarks");
            order.setExtInfo("extInfo");
            // 自动更新时间
            order.setType(0);
            orders.add(order);
        }
        return orders;
    }

    @Test
    public void insert_batch_column_test() {
        List<Order> orders = createOrders();
        long startTime = System.nanoTime();
        orderMapper.insertBatchSomeColumn(orders);
        long endTime = System.nanoTime();
        System.out.println(endTime - startTime); // 6_662_785_700  6s
    }

    @Test
    public void save_batch_test() {
        List<Order> orders = createOrders();
        long startTime = System.nanoTime();
        orderService.saveBatch(orders);
        long endTime = System.nanoTime();
        System.out.println(endTime - startTime); // 27_099_770_500  27s
    }

}

package com.snippet.spring.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snippet.spring.dao.entity.Order;
import com.snippet.spring.dao.mapper.OrderMapper;
import com.snippet.spring.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * create by whr on 2023/2/19
 */
@SpringBootTest
public class SelectTest {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderService orderService;

    @Test
    public void select_by_lambda_test() {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(Order::getId, 1627314511772188795L);
        List<Order> res = orderMapper.selectList(queryWrapper);
        System.out.println(res.size());
    }

    @Test
    public void select_page_test() {
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(Order::getId, 1627314511772188795L);
        Page<Order> page = new Page<>(1, 100);
        Page<Order> res = orderMapper.selectPage(page, queryWrapper);
        System.out.println(res.getTotal());  // 总数
        System.out.println(res.getPages()); // 总页
        System.out.println(res.getCurrent());   // 当前页
        System.out.println(res.getSize());  // 当前页记录数
        List<Order> records = res.getRecords();
        System.out.println(records.size()); // 100
    }
}

package com.snippet.springtransaction.utils;

import cn.hutool.core.util.IdUtil;
import com.snippet.springtransaction.dao.entity.Order;

import java.time.LocalDateTime;

/**
 * create by whr on 2023/3/3
 */
public class OrderUtils {

    public static Order getOrder() {
        Order order = new Order();
        order.setId(IdUtil.getSnowflakeNextId());
        order.setUserId(1111L);
        order.setAmount(123456L);
        order.setStatus(0);
        order.setChannel("paypal");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return order;
    }
}

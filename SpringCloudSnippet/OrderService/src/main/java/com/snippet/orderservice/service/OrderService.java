package com.snippet.orderservice.service;
import com.snippet.orderservice.clients.UserClient;
import com.snippet.orderservice.dao.OrderMapper;
import com.snippet.orderservice.dao.entity.Order;
import com.snippet.orderservice.dao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserClient userClient;

    public Order queryOrderById(String orderId) {
        Order order = orderMapper.findById(orderId);
        User user = userClient.findById(order.getUserId());
        order.setUser(user);
        return order;
    }
}

package com.snippet.spring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snippet.spring.dao.entity.Order;
import com.snippet.spring.dao.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:41:01
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IService<Order> {

}

package com.snippet.spring.service.impl;

import com.snippet.spring.dao.entity.OrderDetail;
import com.snippet.spring.dao.mapper.OrderDetailMapper;
import com.snippet.spring.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:41:01
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}

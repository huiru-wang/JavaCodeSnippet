package com.snippet.spring.dao.mapper;

import com.snippet.spring.dao.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderInfoMapper {
    int deleteByPrimaryKey(@Param("id") Long id, @Param("orderId") Long orderId);

    int insert(OrderInfo row);

    int insertSelective(OrderInfo row);

    OrderInfo selectByPrimaryKey(@Param("id") Long id, @Param("orderId") Long orderId);

    int updateByPrimaryKeySelective(OrderInfo row);

    int updateByPrimaryKey(OrderInfo row);
}
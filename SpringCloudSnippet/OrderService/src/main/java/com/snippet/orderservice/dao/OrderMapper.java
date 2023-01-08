package com.snippet.orderservice.dao;

import com.snippet.orderservice.dao.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    @Select("select * from t_order where id = #{id}")
    Order findById(@Param("id") String id);
}


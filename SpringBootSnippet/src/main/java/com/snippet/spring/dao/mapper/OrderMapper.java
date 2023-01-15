package com.snippet.spring.dao.mapper;

import com.snippet.spring.dao.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper {
    int deleteByPrimaryKey(@Param("id") Long id, @Param("userId") Long userId);

    int insert(Order row);

    int insertSelective(Order row);

    Order selectByPrimaryKey(@Param("id") Long id, @Param("userId") Long userId);

    int updateByPrimaryKeySelective(Order row);

    int updateByPrimaryKey(Order row);
}
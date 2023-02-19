package com.snippet.spring.dao.mapper;

import com.snippet.spring.dao.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:36:03
 */
@Mapper
public interface OrderMapper extends BatchBaseMapper<Order> {

    // 测试自定义SQL，自动更新时间
    Integer insertOrder(@Param("order") Order order);
}

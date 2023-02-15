package com.snippet.spring.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snippet.spring.dao.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:36:03
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    Integer insertOrder(@Param("order") Order order);

    Integer insertBatchSomeColumn(Collection<Order> entityList);
}

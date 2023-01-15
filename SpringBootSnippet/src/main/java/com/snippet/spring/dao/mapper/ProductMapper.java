package com.snippet.spring.dao.mapper;

import com.snippet.spring.dao.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product row);

    int insertSelective(Product row);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Product row);

    int updateByPrimaryKey(Product row);
}
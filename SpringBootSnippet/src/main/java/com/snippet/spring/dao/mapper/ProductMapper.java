package com.snippet.spring.dao.mapper;

import com.snippet.spring.dao.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:36:04
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}

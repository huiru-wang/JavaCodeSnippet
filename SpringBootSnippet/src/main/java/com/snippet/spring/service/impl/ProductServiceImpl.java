package com.snippet.spring.service.impl;

import com.snippet.spring.dao.entity.Product;
import com.snippet.spring.dao.mapper.ProductMapper;
import com.snippet.spring.service.ProductService;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}

package com.snippet.spring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snippet.spring.dao.entity.Product;
import com.snippet.spring.dao.mapper.ProductMapper;
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
public class ProductService extends ServiceImpl<ProductMapper, Product> implements IService<Product> {

}

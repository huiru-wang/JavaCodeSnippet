package com.snippet.spring.dao.entity;

import lombok.Data;

@Data
public class Product {
    private Long id;

    private String productName;

    private Long price;

    private String productInfo;
  
}
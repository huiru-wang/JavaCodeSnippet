package com.snippet.spring.dao.entity;

import lombok.Data;

@Data
public class Order {
    private Long id;

    private Long userId;

    private String remarks;
}
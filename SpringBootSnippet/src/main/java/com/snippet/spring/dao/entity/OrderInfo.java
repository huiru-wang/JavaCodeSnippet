package com.snippet.spring.dao.entity;

import lombok.Data;

@Data
public class OrderInfo {
    private Long id;

    private Long orderId;

    private Long productId;

    private Long quantity;

    private String remarks;
}
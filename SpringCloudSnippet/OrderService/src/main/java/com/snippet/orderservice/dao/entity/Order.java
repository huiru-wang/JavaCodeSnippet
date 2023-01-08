package com.snippet.orderservice.dao.entity;

import lombok.Data;

@Data
public class Order {

    private String id;

    private String userId;

    private String Item;

    private Long price;

    private Integer quantity;

    private User user;
}
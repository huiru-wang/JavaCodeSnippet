package com.snippet.orderservice.dao.entity;

import lombok.Data;

@Data
public class User {

    private Long id;

    private String userName;

    private String phone;

    private String address;
}
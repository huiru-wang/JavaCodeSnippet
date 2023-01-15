package com.snippet.spring.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private Date updateTime;
    private Date createTime;
}
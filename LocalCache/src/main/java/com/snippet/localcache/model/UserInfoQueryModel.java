package com.snippet.localcache.model;

import lombok.Data;

/**
 * create by whr on 2023/2/19
 */
@Data
public class UserInfoQueryModel {

    private String username;

    private String addr;

    public UserInfoQueryModel(String username, String addr) {
        this.username = username;
        this.addr = addr;
    }
}

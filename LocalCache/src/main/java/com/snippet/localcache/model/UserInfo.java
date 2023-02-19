package com.snippet.localcache.model;

import lombok.Data;

/**
 * create by whr on 2023/2/19
 */
@Data
public class UserInfo {

    private String username;

    private String addr;

    private String email;

    public UserInfo(String username, String addr, String email) {
        this.username = username;
        this.addr = addr;
        this.email = email;
    }
}

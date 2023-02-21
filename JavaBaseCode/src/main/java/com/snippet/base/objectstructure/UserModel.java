package com.snippet.base.objectstructure;

public class UserModel {

    private Long userId;

    private String username;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserModel() {
    }

    public UserModel(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

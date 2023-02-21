package com.atguigu.chapter12;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

public class LoginEvent {
    public String userId;
    public String ipAddress;
    public String eventType;
    public Long timestamp;

    public LoginEvent(String userId, String ipAddress, String eventType, Long timestamp) {
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    public LoginEvent() {}

    @Override
    public String toString() {
        return "LoginEvent{" +
                "userId='" + userId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}


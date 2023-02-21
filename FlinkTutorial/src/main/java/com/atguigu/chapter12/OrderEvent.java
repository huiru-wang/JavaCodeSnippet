package com.atguigu.chapter12;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

public class OrderEvent {
    public String userId;
    public String orderId;
    public String eventType;
    public Long timestamp;

    public OrderEvent() {
    }

    public OrderEvent(String userId, String orderId, String eventType, Long timestamp) {
        this.userId = userId;
        this.orderId = orderId;
        this.eventType = eventType;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "userId='" + userId + '\'' +
                "orderId='" + orderId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}


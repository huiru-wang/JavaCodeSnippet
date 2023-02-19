package com.snippet.kafkaexample.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderMessage {

    private String orderId;

    private String userId;

    private String orderInfo;

    private LocalDateTime orderTime;

    public OrderMessage() {

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}

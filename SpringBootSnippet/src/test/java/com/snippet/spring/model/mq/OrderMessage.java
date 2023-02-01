package com.snippet.spring.model.mq;


import lombok.Builder;

@Builder
public class OrderMessage {

    private String orderId;

    private String userId;

    private String orderInfo;
}

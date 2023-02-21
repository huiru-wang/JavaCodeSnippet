package com.snippet.base.objectstructure;

public class OrderModel {

    private long orderId;

    private UserModel userInfo;

    public String orderInfo;

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setUserInfo(UserModel userInfo) {
        this.userInfo = userInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}

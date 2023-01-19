package com.snippet.spring.optimization.model;

import lombok.Data;

@Data
public class OrderModel {

    private String a0;
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;
    private String a6;
    private String a7;
    private String a8;
    private String a9;

    public OrderModel(String a0, String a1, String a2, String a3, String a4, String a5, String a6, String a7, String a8, String a9) {
        this.a0 = a0;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.a5 = a5;
        this.a6 = a6;
        this.a7 = a7;
        this.a8 = a8;
        this.a9 = a9;
    }
}

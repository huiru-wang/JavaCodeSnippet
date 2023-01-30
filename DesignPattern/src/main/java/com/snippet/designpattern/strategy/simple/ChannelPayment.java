package com.snippet.designpattern.strategy.simple;

import com.snippet.designpattern.strategy.combination.PaymentStrategy;

public class ChannelPayment {

    private PaymentStrategy paymentStrategy;

    public ChannelPayment(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void doPayment(){
        System.out.println("Do Payment");
        this.paymentStrategy.payment();
    }
}

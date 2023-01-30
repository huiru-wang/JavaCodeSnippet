package com.snippet.designpattern.strategy.combination;

/**
 * 可以拓展为模板方法
 * 增加：
 * payment, refund, query...
 */
public class ChannelPayment {


    public void doPayment(String channel){
        System.out.println("Do Payment");
        PaymentStrategy paymentStrategy = PaymentChannelFactory.getPaymentStrategy(channel);
        paymentStrategy.payment();
    }
}

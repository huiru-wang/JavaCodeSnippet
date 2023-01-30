package com.snippet.designpattern.strategy.simple.concrete;

import com.snippet.designpattern.strategy.combination.PaymentStrategy;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public boolean payment() {
        System.out.println("PayPal Pay");
        return true;
    }
}

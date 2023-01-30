package com.snippet.designpattern.strategy.simple.concrete;

import com.snippet.designpattern.strategy.combination.PaymentStrategy;

public class AdyenPayment implements PaymentStrategy {
    @Override
    public boolean payment() {
        System.out.println("Adyen Pay");
        return true;
    }
}
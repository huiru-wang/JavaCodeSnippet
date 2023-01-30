package com.snippet.designpattern.strategy.simple.concrete;

import com.snippet.designpattern.strategy.combination.PaymentStrategy;

public class KnetPayment implements PaymentStrategy {
    @Override
    public boolean payment() {
        System.out.println("Knet Pay");
        return true;
    }
}

package com.snippet.designpattern.strategy.combination.concrete;

import com.snippet.designpattern.strategy.combination.PaymentStrategy;


public class AdyenPayment implements PaymentStrategy {
    @Override
    public boolean payment() {
        return true;
    }
}
package com.snippet.designpattern.strategy.combination.concrete;

import com.snippet.designpattern.strategy.combination.PaymentStrategy;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public boolean payment() {
        return true;
    }
}

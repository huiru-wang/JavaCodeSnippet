package com.snippet.designpattern.strategy;

import com.snippet.designpattern.strategy.combination.PaymentStrategy;
import com.snippet.designpattern.strategy.simple.ChannelPayment;
import com.snippet.designpattern.strategy.simple.concrete.AdyenPayment;

public class App {
    public static void main(String[] args) {
        // simple
        AdyenPayment adyenPayment = new AdyenPayment();
        ChannelPayment channelPayment = new ChannelPayment(adyenPayment);
        channelPayment.doPayment();

        // combination:
        Package aPackage = PaymentStrategy.class.getPackage();
    }
}

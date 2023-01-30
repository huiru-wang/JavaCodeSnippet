package com.snippet.designpattern.strategy.combination;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentChannelFactory {

    private static final Map<String, PaymentStrategy> paymentStrategyMap = new ConcurrentHashMap<>(16);

    static {
        // 初始化加载所有PaymentChannel
        Class<? super PaymentStrategy> superclass = PaymentStrategy.class.getSuperclass();
    }

    public static PaymentStrategy getPaymentStrategy(String channel){
        return paymentStrategyMap.get(channel);
    }

}

package com.snippet.designpattern.strategy.combination;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentChannelFactory {

    private static final Map<String, PaymentStrategy> paymentStrategyMap = new ConcurrentHashMap<>(16);

    static {
        // 初始化加载所有PaymentChannel，可以借助Spring容器获取接口的实现，这里简单单例处理
        AdyenPayment adyenPayment = AdyenPayment.newInstance();
        // 获取注解支付方式
        PaymentChannel paymentChannel = adyenPayment。getClass().getAnnotation(PaymentChannel.class);
        
        paymentStrategyMap.put(paymentChannel.channel(), adyenPayment);
    }

    public static PaymentStrategy getPaymentStrategy(String channel){
        return paymentStrategyMap.get(channel);
    }

}

package com.snippet.spring.listeners;

import com.alibaba.fastjson.JSON;
import com.snippet.spring.model.mq.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@Configuration
public class OrderListener {

    /**
     * clientIdPrefix：客户端id前缀，覆盖kafka.consumer.client-id
     * id：消费者线程id
     * topic：topic
     * concurrency：并发度
     */
    @KafkaListener(clientIdPrefix = "order-listener", id = "snippet-client-0", topics = "snippet-test",
            concurrency = "1", errorHandler = "kafkaDefaultListenerErrorHandler")
    public void onMessage(String message) {
        log.info("Order Listener on message: {}", message);
        OrderMessage orderMessage = JSON.parseObject(message, OrderMessage.class);
    }
}

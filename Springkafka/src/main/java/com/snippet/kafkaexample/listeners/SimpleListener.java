package com.snippet.kafkaexample.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Kafka Listener 示例
 */
@Slf4j
@Configuration
public class SimpleListener {

    /**
     * clientIdPrefix：客户端id前缀，覆盖kafka.consumer.client-id
     * id：listener名，可以获取进行配置
     * topic：topic
     * concurrency：并发度
     * errorHandler: 指定异常handler
     */
    @KafkaListener(
            id = "simpleListener",
            topics = {"snippet-test"},
            concurrency = "1"
    )
    public void onMessage(String message) {
        log.info("Order Listener on message: {}", message);
    }
}

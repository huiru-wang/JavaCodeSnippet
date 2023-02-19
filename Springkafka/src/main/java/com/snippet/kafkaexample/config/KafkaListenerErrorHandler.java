package com.snippet.kafkaexample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerAwareErrorHandler;

import java.util.Objects;

/**
 * 需要 Listener注解中指定 errorHandler
 * 可以在这里失败后，处理offset
 */
@Slf4j
@Configuration
public class KafkaListenerErrorHandler {

    public ConsumerAwareErrorHandler consumerAwareErrorHandler() {
        return (exception, consumerRecord, consumer) -> {
            if (Objects.nonNull(consumerRecord)) {
                String topic = consumerRecord.topic();
                Object key = consumerRecord.key();
                int partition = consumerRecord.partition();
                long offset = consumerRecord.offset();
                log.error("consume failed: topic:{}, key:{}, partition:{}, offset:{}", topic, key, partition, offset);
            } else {
                log.error("consume failed: ", exception);
            }
        };
    }
}

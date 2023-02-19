package com.snippet.kafkaexample.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

/**
 * Kafka Listener 示例
 * 具体listener可以接受什么参数，详见：@KafkaListener的Doc
 */
@Slf4j
@Configuration
public class RecordListener {
    /**
     * 获取消费Record 信息
     */
    @KafkaListener(
            containerFactory = "kafkaListenerContainerFactory",
            id = "recordListener",
            topics = {"snippet-order"},
            concurrency = "1"
    )
    public void onConsumerRecord(List<ConsumerRecord<String, String>> records) {
        // 批量拉取
        ConsumerRecord<String, String> record = records.get(0);
        int partition = record.partition();
        String topic = record.topic();
        String key = record.key();
        String value = record.value();
        log.info("Order Listener on message: topic:{},partition:{},key:{},value:{}", topic, partition, key, value);
    }
}


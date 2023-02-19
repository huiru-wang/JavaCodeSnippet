package com.snippet.kafkaexample.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * 同步commit
 */
@Slf4j
@Configuration
public class CommitSyncListener {

    @KafkaListener(
            containerFactory = "kafkaListenerContainerFactory",
            id = "commitSyncListener",
            topics = {"snippet-order"},
            concurrency = "1"
    )
    public void onConsumerRecord(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        int partition = record.partition();
        String topic = record.topic();
        String key = record.key();
        String value = record.value();
        log.info("Order Listener on message: topic:{},partition:{},key:{},value:{}", topic, partition, key, value);
        // ...

        // manual commit
        consumer.commitSync();
        // consumer.commitAsync();
    }
}

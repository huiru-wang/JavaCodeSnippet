package com.snippet.kafkaexample.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;
import java.util.Objects;

/**
 * 异步commit
 */
@Slf4j
@Configuration
public class CommitAsyncListener {
    @KafkaListener(
            containerFactory = "kafkaListenerContainerFactory",
            id = "commitAsyncListener",
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
        // consumer.commitAsync();
        consumer.commitAsync(new OffsetCommitCallback() {
            @Override
            public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                if (Objects.isNull(exception)) {
                    log.info("commit offset: {}", offsets);
                } else {
                    log.error("commit error: ", exception);
                }
            }
        });
    }
}

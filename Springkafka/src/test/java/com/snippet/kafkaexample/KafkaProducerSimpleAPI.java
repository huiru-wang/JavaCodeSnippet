package com.snippet.kafkaexample;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.snippet.kafkaexample.model.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class KafkaProducerSimpleAPI {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    String topicName = "snippet-test";

    String messageKey = "order-key";

    String messageValue = null;
    OrderMessage orderMessage = new OrderMessage();

    @BeforeEach
    public void init() {
        orderMessage.setOrderId(IdUtil.simpleUUID());
        orderMessage.setUserId(IdUtil.simpleUUID());
        orderMessage.setOrderInfo("test-order");
        orderMessage.setOrderTime(LocalDateTime.now());
        messageValue = JSON.toJSONString(orderMessage);
    }

    /**
     * send 为异步发送
     */
    @Test
    public void async_send() {
        kafkaTemplate.send(topicName, 0, messageKey, messageValue); // 1、指定partition
        kafkaTemplate.send(topicName, messageKey, messageValue); // 2、默认hash分区
        kafkaTemplate.send(topicName, messageValue); // 3、选择粘性分区，尽可能放在最多的batch中，满了能尽快发送
        // 4、自定义分区实现Partitioner接口
    }

    /**
     * add callback
     */
    @Test
    public void async_send_with_callback() throws InterruptedException {
        kafkaTemplate.send(topicName, messageKey, messageValue)
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("send fail: ", ex);
                    }

                    @Override
                    public void onSuccess(SendResult<String, String> result) {
                        ProducerRecord<String, String> producerRecord = result.getProducerRecord();
                        log.info("producer record: {}", producerRecord.toString());

                        RecordMetadata recordMetadata = result.getRecordMetadata();
                        long offset = recordMetadata.offset();
                        log.info("record offset: {}", offset);
                    }
                });
        Thread.sleep(1000);
    }

    /**
     * send 本为异步发送，如果要同步，使用Future.get()阻塞等待获取结果；
     */
    @Test
    public void sync_send() {
        SendResult<String, String> sendResult = null;
        try {
            sendResult = kafkaTemplate.send(topicName, messageKey, messageValue).get();
        } catch (Exception e) {
            log.error("send fail: ", e);
        }
        log.info("sendResult: {}", sendResult);
    }

    @Test
    public void sync_send_record() {
        //  kafka 提供的消息封装类
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, messageKey, messageValue);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);
        SendResult<String, String> sendResult = null;
        try {
            sendResult = future.get();
        } catch (Exception e) {
            log.error("send fail: ", e);
        }
        log.info("sendResult: {}", sendResult);
    }
}

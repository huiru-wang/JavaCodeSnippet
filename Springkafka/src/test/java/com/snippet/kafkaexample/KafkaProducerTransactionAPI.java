package com.snippet.kafkaexample;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.snippet.kafkaexample.model.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class KafkaProducerTransactionAPI {
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
     * 使用事务前提：开启幂等("enable.idempotence": true)
     * kafkaTemplate executeInTransaction已经封装了事务流程：
     * 1. producer.beginTransaction()
     * 2. beginTransaction()
     * 3. commitTransaction()
     * 4. abortTransaction()
     */
    @Test
    public void transaction_send() {
        kafkaTemplate.executeInTransaction(kafkaOperations -> {
            // 抛出异常，则abortTransaction，回滚
            return kafkaOperations.send(topicName, messageKey, messageValue);
        });
    }

}

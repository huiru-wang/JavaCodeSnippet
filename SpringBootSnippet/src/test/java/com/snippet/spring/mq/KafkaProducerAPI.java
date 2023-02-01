package com.snippet.spring.mq;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.snippet.spring.model.mq.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@SpringBootTest
public class KafkaProducerAPI {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    String topicName = "snippet-test";

    @Test
    public void sync_send() {
        String messageKey = "snippet_key";
        OrderMessage orderMessage = OrderMessage.builder()
                .orderId(IdUtil.fastUUID())
                .userId(IdUtil.simpleUUID())
                .orderInfo("nice")
                .build();

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, messageKey, JSON.toJSONString(orderMessage));
        SendResult<String, String> sendResult = null;
        try {
            sendResult = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("sendResult: {}", sendResult);
    }

    @Test
    public void simple_test() {
        kafkaTemplate.send(topicName, "snippet-key", "snippet-value");
    }
}

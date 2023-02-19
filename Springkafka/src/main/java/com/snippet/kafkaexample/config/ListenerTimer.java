package com.snippet.kafkaexample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 定时开启Listener
 * 1、关闭Listener自动开启
 * 2、设置定时器，通过 listener-id 开启指定Listener
 */
@Slf4j
@Component
@EnableScheduling
public class ListenerTimer {

    @Autowired
    private KafkaListenerEndpointRegistry registry; // TODO

    @Scheduled(cron = "0 42 11 * * ? ")
    public void startListener() {
        MessageListenerContainer simpleListener = registry.getListenerContainer("simpleListener");
        if (Objects.isNull(simpleListener)) {
            log.warn("simpleListener is null, start failed.");
            return;
        }
        if (!simpleListener.isRunning()) {
            simpleListener.start();
        }
    }

    @Scheduled(cron = "0 20 11 * * ? ")
    public void stopListener() {
        MessageListenerContainer simpleListener = registry.getListenerContainer("simpleListener");
        if (Objects.isNull(simpleListener)) {
            log.warn("simpleListener is null, start failed.");
            return;
        }
        if (simpleListener.isRunning()) {
            simpleListener.stop();
        }
    }
}

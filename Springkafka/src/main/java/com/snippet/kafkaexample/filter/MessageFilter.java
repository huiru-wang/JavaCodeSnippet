package com.snippet.kafkaexample.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageFilter {

    @Autowired
    ConsumerFactory<String, String> consumerFactory;

}

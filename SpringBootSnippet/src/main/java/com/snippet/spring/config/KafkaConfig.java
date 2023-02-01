package com.snippet.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.validation.Validator;

@EnableKafka
@Configuration
public class KafkaConfig implements KafkaListenerConfigurer {

    @Autowired
    private Validator Validator;

    /**
     * kafka listener 入参可以使用@Valid 进行参数校验
     */
    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar kafkaListenerEndpointRegistrar) {
        kafkaListenerEndpointRegistrar.setValidator(Validator);
    }

}

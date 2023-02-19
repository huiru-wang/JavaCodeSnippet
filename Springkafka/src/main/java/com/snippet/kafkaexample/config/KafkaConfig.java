package com.snippet.kafkaexample.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.core.*;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka配置项官方文档：https://kafka.apache.org/21/documentation.html
 * Spring-kafka配置文档：https://docs.spring.io/spring-kafka/docs/current/reference/html/
 * 配置项点进去，拿到字符串，到官网搜即可直到含义，默认值等
 */
@EnableKafka
@Configuration
public class KafkaConfig implements KafkaListenerConfigurer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * 消费配置
     */
    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // kafka的__consumer_topic会维护消费者消费topic的offset，但是如果没有记录当前的消费者，那么消费者从哪个offset开始消费？
        // 由"auto.offset.reset"决定
        // earliest: automatically reset the offset to the earliest offset
        // latest: automatically reset the offset to the latest offset
        // none: throw exception
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 默认latest，从最新消费
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);  // 设为true，则kafka后台定期提交offset
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000); // 自动提交频率
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);    // 一次单独的poll 拉取多少消息，默认500
        // props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, )    // partition分配策略
        // ...
        return props;
    }

    /**
     * listener 工厂
     * 可以配置多个，listener注解通过containerFactory = "kafkaListenerContainerFactory"指定
     */
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1); // 并发度
        // 开启listener批量消费,每批多少条由："max.poll.records"决定，即：ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(true);

        // 消费端过滤器
        factory.setRecordFilterStrategy(consumerRecord -> {
            // 返回false则 丢弃消息
            return true;
        });
        // ...
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    /**
     * 生产 配置
     */
    private Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 二者满足一个，即触发push
        props.put(ProducerConfig.LINGER_MS_CONFIG, 100);    // 提交触发最短间隔
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 1048576); // 提交触发最小size
        props.put(ProducerConfig.ACKS_CONFIG, "all");   // 默认为1(最少一次) all/-1 精确一次
        props.put(ProducerConfig.RETRIES_CONFIG, 1);    // 默认为0，重试任何发送失败的请求
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 批量发送
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 生产者发送内存，默认32Mb

        // 实现发送 精确一次，kafka对每个消息生成唯一Id，做到去重；仅能保证但分区幂等
        // 如果需要强一致性，需要开启事务，前提也需开启此配置
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        // props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "myPartitionStrategy.class"); // 自定义分区策略
        // props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, "myInterceptor.class"); // 自定义拦截器
        //...
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(producerProps());
        // 生产者事务Id前缀
        factory.setTransactionIdPrefix("tx-");

        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTransactionManager<String, String> kafkaAwareTransactionManager(ProducerFactory<String, String> factory) {
        // 配置 KafkaTransactionManager
        return new KafkaTransactionManager<>(factory);
    }

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {

    }
}

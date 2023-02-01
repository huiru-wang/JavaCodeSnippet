package com.snippet.spring.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Value("${spring.redis.host:ubuntu.wsl}")
    private String host;

    @Value("${spring.redis.port:6379}")
    private int port;
    @Value("${spring.redis.password:123456}")
    private String password;

    @Value("${spring.redis.database:0}")
    private int database;
    @Value("${spring.redis.connect-timeout:1000}")
    private int timeout;

    @Autowired
    LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 序列化对象非final
        // LaissezFaireSubTypeValidator.instance: 序列化同时 保存类名，用于返序列化
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        // value serializer
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // value serializer
        template.setValueSerializer(jackson2JsonRedisSerializer);

        // key serializer
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);

        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }

    @Bean
    public HashOperations<String, String, Object> hashOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForHash();
    }

    @Bean
    public ValueOperations<String, String> valueOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForValue();
    }

    @Bean
    public ListOperations<String, String> listOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForList();
    }

    @Bean
    public SetOperations<String, String> setOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForSet();
    }

    @Bean
    public ZSetOperations<String, String> zSetOperations(StringRedisTemplate stringRedisTemplate) {
        return stringRedisTemplate.opsForZSet();
    }

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 集群
        // config.useClusterServers().addNodeAddress("172.27.172.54:6379", "");
        // 单机
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port)
                .setDatabase(0)
                .setPassword(password)
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(24)
                .setConnectTimeout(10000)
                .setIdleConnectionTimeout(10000)
                .setRetryAttempts(3)
                .setRetryInterval(1500);
        config.setLockWatchdogTimeout(30 * 1000);   // 续锁的看门狗线程，检查锁的超时时间

        return Redisson.create(config);
    }
}

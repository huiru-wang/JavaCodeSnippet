package com.snippet.spring.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;


@EnableCaching // 开启Spring-Cache
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

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);

        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxTotal(24);
        genericObjectPoolConfig.setMaxIdle(20);
        genericObjectPoolConfig.setMinIdle(10);
        genericObjectPoolConfig.setMaxWait(Duration.ofMillis(timeout));

        LettucePoolingClientConfiguration poolingClientConfiguration = LettucePoolingClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(1000))
                .poolConfig(genericObjectPoolConfig)
                .build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, poolingClientConfiguration);

        stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return stringRedisTemplate;
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

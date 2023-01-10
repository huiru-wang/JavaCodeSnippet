package com.snippet.spring.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 集群
        // config.useClusterServers().addNodeAddress("172.27.172.54:6379", "");
        // 单机
        config.useSingleServer()
            .setAddress("redis://192.168.86.177:6379")
            .setDatabase(1)
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

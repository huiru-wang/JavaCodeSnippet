package com.snippet.spring.service;

import com.snippet.spring.model.NestingObj;
import com.snippet.spring.model.UserCache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 缓存的key默认生成策略： value::key
 * 可以通过keyGenerator()重写生成策略
 */
@Service
public class HelloService {

    /**
     * 缓存
     */
    @Cacheable(key = "#key", value = "cache_demo", cacheManager = "redisCacheManager")
    public UserCache cache(String key) {
        UserCache userCache = new UserCache();
        userCache.setUsername("ddd");
        userCache.setNestingObj(new NestingObj());
        return userCache;
    }

    @CacheEvict(key = "#key", value = "cache_demo", cacheManager = "redisCacheManager")
    public void invalidCache(String key) {
    }

    private static int RETRY_TIMES = 2;

    public ResponseEntity<String> retryBadRequest() {
        while (RETRY_TIMES > 0) {
            return badRequest();
        }
        RETRY_TIMES = 2;
        return ResponseEntity.status(200).body("success");
    }

    public ResponseEntity<String> badRequest() {
        return ResponseEntity.status(400).build();
    }
}

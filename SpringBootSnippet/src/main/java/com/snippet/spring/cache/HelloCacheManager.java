package com.snippet.spring.service.cache;

import com.snippet.spring.model.NestingObj;
import com.snippet.spring.model.UserCache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 缓存的key默认生成策略： value::key
 * 可以通过keyGenerator()重写生成策略
 */
@Service
public class HelloCacheManager {

    @Cacheable(key = "#key",value = "cache_demo")
    public UserCache cache(String key){
        UserCache userCache = new UserCache();
        userCache.setUsername("ddd");
        userCache.setNestingObj(new NestingObj());
        return userCache;
    }

    @CacheEvict(key = "#key",value = "cache_demo")
    public void invalidCache(String key){
    }
}

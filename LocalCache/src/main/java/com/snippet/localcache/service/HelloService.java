package com.snippet.localcache.service;

import cn.hutool.core.collection.ListUtil;
import com.snippet.localcache.model.UserInfo;
import com.snippet.localcache.model.UserInfoQueryModel;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by whr on 2023/2/19
 */
@Service
public class HelloService {

    /**
     * cache_name: user_info
     * cache_key: listUserInfo_UserInfoQueryModel(username=xxx,addr=xxx)
     */
    @Cacheable(cacheNames = "user_info", keyGenerator = "paramKeyGenerator", cacheManager = "caffeineCacheManager")
    public List<UserInfo> listUserInfo(UserInfoQueryModel userInfoQueryModel) {
        String username = userInfoQueryModel.getUsername();
        String addr = userInfoQueryModel.getAddr();
        return ListUtil.of(
                new UserInfo(username, addr, "xiaogang@xx,.com"),
                new UserInfo(username, addr, "xiaoga@xx,.com")
        );
    }

    /**
     * allEntries:失效cacheNames下所有缓存
     */
    @CacheEvict(cacheNames = "user_info", allEntries = true, cacheManager = "caffeineCacheManager")
    public void updateUserInfo() {
    }

    // TODO 失效指定key的缓存

    @CacheEvict(cacheNames = "user_info", allEntries = true, cacheManager = "caffeineCacheManager")
    public void addUserInfo() {
    }

    @CacheEvict(cacheNames = "user_info", allEntries = true, cacheManager = "caffeineCacheManager")
    public void deleteUserInfo() {
    }
}

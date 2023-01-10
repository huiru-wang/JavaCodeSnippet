package com.snippet.spring.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void redisson_rlock_test() {
        RLock lock = redissonClient.getLock("demo_test");
        // 上锁，创建hash key，field为线程唯一ID，value为重入次数
        lock.lock(1, TimeUnit.MINUTES);
        lock.lock(1, TimeUnit.MINUTES); // value++

        // 上锁失败，则循环不断重试，直到获取锁
    }

    @Test
    public void redisson_trylock_test() {
        RLock lock = redissonClient.getLock("demo_test");
        boolean res = false;
        try {
            res = lock.tryLock(10, 100, TimeUnit.SECONDS);
            if (res) {
                log.info("get lock success");
                Thread.sleep(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("get lock error:{}", e.getMessage());
        } finally {
            if (res) {
                lock.unlock();
            }
        }
        if (!res) {
            log.error("get lock failed");
        }
    }

    @Test
    public void redisson_fair_lock_test() {
        RLock fairLock = redissonClient.getFairLock("fair_lock_test");

    }

    @Test
    public void redisson_read_write_lock_test() {
        RReadWriteLock rwlock = redissonClient.getReadWriteLock("read_writ_lock_test");
        boolean res = false;
        try {
            res = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
            // rwlock.writeLock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (res) {
                rwlock.readLock().unlock();
            }
        }
    }

    @Test
    public void redisson_red_lock_test() {
        // 集群场景下，防止master上锁成功后宕机，slave未来得及同步锁，导致自动解锁的情况；

    }
}

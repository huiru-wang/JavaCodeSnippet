package com.snippet.base.concurrency.pool.custompool;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 自定义一个线程池
 * <p>
 * create by whr on 2023/2/15
 */
public class CustomPoolTest {

    @Test
    public void run_custom_pool() throws InterruptedException {
        long start = System.currentTimeMillis();

        ThreadPool threadPool = new ThreadPool(5, 10);

        Random random = new Random();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            threadPool.execute(() -> {
                list.add(random.nextInt());
            });
        }

        threadPool.shutDown();

        System.out.println(list.size());
        long end = System.currentTimeMillis();
        System.out.println("time cost: " + (end - start));
    }
}

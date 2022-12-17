package com.snippet.concurrency.pool.poolmode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreadMode {

    /**
     * 对比使用线程池
     * 线程创建、调度、上下文切换耗时
     */
    public static void start() throws InterruptedException {

        long start = System.currentTimeMillis();
        List<Integer> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 100000; i++) {
            Thread thread = new Thread(() -> {
                list.add(random.nextInt());
            });
            thread.start();
            thread.join();
        }
        long end = System.currentTimeMillis();
        System.out.println("time cost: " + (end - start)); // 27528ms;
        System.out.println(list.size());
    }
}

package com.snippet.concurrency.pool.poolmode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleThreadExecutorMode {

    public static void start() throws InterruptedException {
        long start = System.currentTimeMillis();

        // 属于计算型任务，单线程比较快
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        List<Integer> list = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 100000; i++) {
            threadPool.submit(() -> {
                list.add(random.nextInt());
            });
        }

        threadPool.shutdown(); // 关闭线程池，等待所有线程执行完毕，才会关闭
        boolean isClosed = threadPool.awaitTermination(1, TimeUnit.MINUTES); //阻塞等待线程池关闭,返回是否关闭成功
        if (isClosed) {
            long end = System.currentTimeMillis();
            System.out.println("time cost: " + (end - start)); // 60-80ms
            System.out.println(list.size());
        }
    }
}

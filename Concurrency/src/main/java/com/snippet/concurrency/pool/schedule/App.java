package com.snippet.concurrency.pool.schedule;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class App {

    public static void main(String[] args) {
        // 
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 10L, MILLISECONDS, new DelayQueue());

        DelayQueue<Delayed> queue = new DelayQueue<>();
        queue.add()
    }
}

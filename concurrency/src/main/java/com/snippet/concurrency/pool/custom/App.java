package com.snippet.concurrency.pool.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
    public static void main(String[] args) throws InterruptedException {
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

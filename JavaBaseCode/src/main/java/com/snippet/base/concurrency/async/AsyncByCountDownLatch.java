package com.snippet.base.concurrency.async;

import java.util.concurrent.CountDownLatch;

public class AsyncByCountDownLatch {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch countDownLatch = new CountDownLatch(2);

        // 使用Thread、Runnable 都没有返回值,适用于没有结果的任务
        Thread task1 = new Thread(() -> {
            try {
                System.out.println("prepare rice");
                Thread.sleep(2000);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread task2 = new Thread(() -> {
            try {
                System.out.println("prepare dishes");
                Thread.sleep(2000);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        task1.start();
        task2.start();

        // 同步阻塞等待任务完成
        countDownLatch.await();
        System.out.println("lunch is ready");
    }

}

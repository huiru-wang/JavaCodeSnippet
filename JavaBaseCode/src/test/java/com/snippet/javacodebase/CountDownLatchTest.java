package com.snippet.javacodebase;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 控制线程执行顺序
 * create by whr on 2023/3/4
 */
public class CountDownLatchTest {

    @Test
    public void count_down_latch_test() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            int num = i;
            new Thread(() -> {
                System.out.println("Sub Task " + num + " is prepare");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    System.out.println("Sub Task " + num + " is ready");
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        System.out.println("Main Task begin");
    }
}

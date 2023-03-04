package com.snippet.javacodebase;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * semaphore用来控制同时访问特定资源的线程数量
 * create by whr on 2023/3/4
 */
public class SemaphoreTest {


    @Test
    public void semaphore_test() throws InterruptedException {
        Semaphore tickets = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            int num = i;
            Thread customer = new Thread(() -> {
                try {
                    tickets.acquire();
                    System.out.println("customer " + num + " get the ticket.");
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    tickets.release();
                    System.out.println("customer " + num + " return the ticket.");
                }
            });
            customer.start();
            //customer.join();
        }
        TimeUnit.SECONDS.sleep(21);
    }
}

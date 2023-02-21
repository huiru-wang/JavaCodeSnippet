package com.snippet.base.concurrency.pool.flow;

/**
 * 线程任务
 * <p>
 * create by whr on 2023/2/15
 */
public class Task implements Runnable {
    private final String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}

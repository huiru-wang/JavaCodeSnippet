package com.snippet.concurrency.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 1、线程会先占用核心池，满了(最大核心+队列)之后去队列等待；
 * 2、队列满了之后，如果还没有达到最大线程数量，继续创建线程；
 * 3、到最大线程数，启动拒绝策略；
 */
public class ThreadPoolWorkFlow {
    public static void main(String[] args) throws InterruptedException {
        // 3+5 线程池内最多只存在8个线程，多出则拒绝
        ThreadPoolExecutor threadPoll = new ThreadPoolExecutor(
                2, 3, 2000,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(5),
                new MyRejectedHandler());

        for (int i = 0; i < 10; i++) {
            String name = "Task_" + i;
            Task task = new Task(name);
            try {
                threadPoll.execute(task);
                System.out.println(
                        "PoolSize: " + threadPoll.getPoolSize() +
                                ",Queue" + threadPoll.getQueue());
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Refused:" + name);
            }
        }

        threadPoll.shutdown();
        threadPoll.awaitTermination(5, TimeUnit.MINUTES); // 阻塞等待线程池关闭,返回是否关闭成功
    }


    static class Task implements Runnable {
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

    static class MyRejectedHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("Rejected：" + r.toString());
        }
    }
}

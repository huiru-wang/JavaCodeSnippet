package com.snippet.concurrency.pool.poolmode;

public class App {
    public static void main(String[] args) throws InterruptedException {
        // 对比使用线程池 线程创建、调度、上下文切换耗时
        ThreadMode.start();// 27528ms;

        // 属于计算型任务，单线程比较快
        SingleThreadExecutorMode.start();// 60-80ms
    }
}

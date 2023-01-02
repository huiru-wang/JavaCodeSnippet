package com.snippet.concurrency.thread;

/**
 * 创建线程只有一种方式：new Thread
 */
public class ThreadStatus {
    public static void main(String[] args) throws InterruptedException {
        MyThread myThread = new MyThread();
        myThread.start();
        myThread.join();

    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("thread is running");
            for (int i = 0; i < 10000; i++) {
                System.out.println(i);
            }
        }
    }
}

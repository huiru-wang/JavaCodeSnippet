package com.snippet.base.concurrency.thread;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * 安全停止线程
 * 需要线程内每次任务开始查看线程状态，自行退出
 * <p>
 * create by whr on 2023/2/15
 */
public class InterruptThreadSafelyTest {

    @Test
    public void safely_interrupt_thread() throws InterruptedException {
        CustomThread customThread = new CustomThread();
        customThread.start();
        TimeUnit.NANOSECONDS.sleep(1);

        customThread.interrupt();
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void safely_interrupt_thread_by_flag() throws InterruptedException {
        CustomThread customThread = new CustomThread();
        customThread.start();
        TimeUnit.NANOSECONDS.sleep(1);

        customThread.cancle(true);
        TimeUnit.SECONDS.sleep(1);
    }

    static class CustomThread extends Thread {

        private boolean cancled;

        public void cancle(boolean cancled) {
            this.cancled = cancled;
        }

        @Override
        public void run() {
            while (true) {
                if (Thread.interrupted() || cancled) {
                    System.out.println("thread safely quit.");
                    return;
                }
                System.out.println("thread is running.");
            }
        }
    }
}

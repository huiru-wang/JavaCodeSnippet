package com.snippet.javacodebase.concurrency.async;

import org.apache.commons.lang3.Validate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AsyncByFutureTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // FutureTask需要传入Callable接口，并返回任务执行的结果
        FutureTask<Integer> futureTask1 = new FutureTask<>(() -> {
            System.out.println("rice is preparing");
            Thread.sleep(2000);
            return 1;
        });
        FutureTask<Integer> futureTask2 = new FutureTask<>(() -> {
            System.out.println("dishes is preparing");
            Thread.sleep(2000);
            return 1;
        });
        new Thread(futureTask1).start();
        new Thread(futureTask2).start();

        // get 同步阻塞获取等待任务结果
        Validate.isTrue(futureTask1.get() == 1);
        Validate.isTrue(futureTask2.get() == 1);

        System.out.println("lunch is ready");
    }
}

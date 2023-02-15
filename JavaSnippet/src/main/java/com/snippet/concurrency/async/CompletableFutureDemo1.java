package com.snippet.concurrency.async;

import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * 任务之间有依赖，需要顺序执行：t1->t2->t3
 */
public class CompletableFutureDemo1 {

    public static void main(String[] args) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("material is preparing");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "down";
        }).thenApply(result -> {        // thenApply 执行完成后，接着执行, 并传入上一个任务的结果
           
            if (Objects.equals("down", result)) {
                System.out.println("dishes is preparing");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return "down";
        });
        String result = future.join();
        Validate.isTrue(Objects.equals("down", result));
        System.out.println("dinner is ready");
    }
}

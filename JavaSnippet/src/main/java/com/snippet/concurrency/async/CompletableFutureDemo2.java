package com.snippet.concurrency.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 多任务并行，最终聚合结果
 * t1、t2、t3并行执行，t4等待三个任务的结果后，执行聚合操作
 */
public class CompletableFutureDemo2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("first course is preparing");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "down";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("second course is preparing");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "down";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            System.out.println("dessert is preparing");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "down";
        });
        CompletableFuture<String> result = CompletableFuture.allOf(future1, future2, future3).thenApply(r -> {
            System.out.println("all down");
            return "dinner is ready";
        });

        String s = result.get();
        System.out.println(s);
    }
}

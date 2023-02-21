package com.snippet.base.concurrency.async;

import java.util.concurrent.CompletableFuture;

/**
 * exceptionally在任意的异步任务后，处理异常
 * applyToEither 增加同时执行的任务，并当有任意任务完成，则继续执行
 */
public class CompletableFutureDemo4 {

    public static void main(String[] args) {

        // 执行两个任务，并等待任意一个完成就结束，并处理异常
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("");
            }
            return "302 bus";
        }).exceptionally(e -> "302 crash").applyToEither(CompletableFuture.supplyAsync(() -> {
            return "703 bus";
        }), firstDown -> {
            return firstDown;
        }).exceptionally(e -> {
            return "unexpected exception";
        });

        String result = future.join();
        System.out.println(result);
    }
}

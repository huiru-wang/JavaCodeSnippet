package com.snippet.concurrency.pool.flow;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 拒绝策略
 * <p>
 * create by whr on 2023/2/15
 */
public class MyRejectedHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("Rejected：" + r.toString());
    }
}

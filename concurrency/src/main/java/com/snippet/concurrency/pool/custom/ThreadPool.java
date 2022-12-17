package com.snippet.concurrency.pool.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {

    private final int maxTask;

    private final BlockingQueue<Runnable> queue;

    private final List<Worker> workers;

    public ThreadPool(int poolSize, int taskSize) {
        this.maxTask = poolSize;
        this.queue = new ArrayBlockingQueue<>(taskSize);
        this.workers = new ArrayList<>();
    }

    public void execute(Runnable task) throws InterruptedException {
        if (task == null) {
            return;
        }
        if (workers.size() < maxTask) {
            Worker worker = new Worker(queue, workers.size());
            workers.add(worker);
            worker.start();
        }
        queue.put(task);
    }

    public void shutDown() {
        try {
            for (Worker worker : workers) {
                worker.interrupt();
                worker.join();
            }
            System.out.println("shutdown success");
        } catch (InterruptedException e) {
            System.out.println("shutdown failed");
        }
    }
}

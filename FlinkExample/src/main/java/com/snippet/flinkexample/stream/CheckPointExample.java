package com.snippet.flinkexample.stream;

import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class CheckPointExample {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(1000);
        // 仅保证flink处理过程的EXACTLY_ONCE，不保证source和sink的 EXACTLY_ONCE
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE); // 稍微提高时延
        env.getCheckpointConfig().setCheckpointTimeout(60000);   // checkpoints超时时间
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);   // 两个checkpoints 最小时间间隔
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);   // 同时可执行的checkpoint个数(运行时最多几个checkpoint)
        env.getCheckpointConfig().setFailOnCheckpointingErrors(true);   // checkpoint失败后是否停止任务，方法过时
        env.getCheckpointConfig().setTolerableCheckpointFailureNumber(1);   // 可以容忍多少个连续检查点故障
    }
}
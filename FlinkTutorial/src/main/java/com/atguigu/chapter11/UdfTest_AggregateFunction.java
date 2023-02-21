package com.atguigu.chapter11;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import com.atguigu.chapter05.ClickSource;
import com.atguigu.chapter05.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class UdfTest_AggregateFunction {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 1. 自定义数据源，从流转换
        SingleOutputStreamOperator<Event> eventStream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ZERO)
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                return element.timestamp;
                            }
                        })
                );

        // 2. 将流转换成表
        Table eventTable = tableEnv.fromDataStream(eventStream,
                $("user"),
                $("url"),
                $("timestamp").as("ts"),
                $("rt").rowtime());
        tableEnv.createTemporaryView("EventTable", eventTable);

        // 3. 注册自定义表函数
        tableEnv.createTemporarySystemFunction("WeightedAverage", WeightedAverage.class);

        // 4. 调用UDF查询转换，这里权重直接给1
        Table resultTable = tableEnv.sqlQuery("select user, " +
                "  WeightedAverage(ts, 1) as weighted_avg " +
                "from EventTable " +
                "group by user");

        // 5. 输出到控制台
        tableEnv.executeSql("create table output (" +
                "uname STRING, " +
                "weighted_avg BIGINT) " +
                "WITH (" +
                "'connector' = 'print')");
        resultTable.executeInsert("output");
    }

    // 单独定义一个累加器类型
    public static class WeightedAvgAccumulator {
        public long sum = 0;    // 加权和
        public int count = 0;    // 数据个数
    }

    // 自定义一个AggregateFunction，求加权平均值
    public static class WeightedAverage extends AggregateFunction<Long, WeightedAvgAccumulator>{
        @Override
        public Long getValue(WeightedAvgAccumulator accumulator) {
            if (accumulator.count == 0)
                return null;    // 防止除数为0
            else
                return accumulator.sum / accumulator.count;
        }

        @Override
        public WeightedAvgAccumulator createAccumulator() {
            return new WeightedAvgAccumulator();
        }

        // 累加计算方法，类似于add
        public void accumulate(WeightedAvgAccumulator accumulator, Long iValue, Integer iWeight){
            accumulator.sum += iValue * iWeight;    // 这个值要算iWeight次
            accumulator.count += iWeight;
        }

    }
}

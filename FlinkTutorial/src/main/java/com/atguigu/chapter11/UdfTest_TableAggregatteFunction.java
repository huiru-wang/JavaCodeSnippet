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
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.*;

public class UdfTest_TableAggregatteFunction {
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

        // 3. 开滚动窗口聚合，得到每个用户在每个窗口中的浏览量
        Table windowAggTable = tableEnv.sqlQuery("select user, count(url) as cnt, " +
                "window_end " +
                "from TABLE(" +
                "  TUMBLE( TABLE EventTable, DESCRIPTOR(rt), INTERVAL '10' SECOND )" +
                ")" +
                "group by user," +
                "  window_start," +
                "  window_end");
        tableEnv.createTemporaryView("AggTable", windowAggTable);

        // 4. 注册表聚合函数函数
        tableEnv.createTemporarySystemFunction("Top2", Top2.class);

        // 5. 在Table API中调用函数
        Table resultTable = tableEnv.from("AggTable")
                .groupBy($("window_end"))
                .flatAggregate(call("Top2", $("cnt")).as("value", "rank"))
                .select($("window_end"), $("value"), $("rank"));

        // 6. 输出到控制台
        tableEnv.toChangelogStream(resultTable).print();

        env.execute();
    }
    // 聚合累加器的类型定义，包含最大的第一和第二两个数据
    public static class Top2Accumulator {
        public Long first;
        public Long second;
    }

    // 自定义表聚合函数，查询一组数中最大的两个，返回值为(数值，排名)的二元组
    public static class Top2 extends TableAggregateFunction<Tuple2<Long, Integer>, Top2Accumulator> {

        @Override
        public Top2Accumulator createAccumulator() {
            Top2Accumulator acc = new Top2Accumulator();
            acc.first = Long.MIN_VALUE;    // 为方便比较，初始值给最小值
            acc.second = Long.MIN_VALUE;
            return acc;
        }

        // 每来一个数据调用一次，判断是否更新累加器
        public void accumulate(Top2Accumulator acc, Long value) {
            if (value > acc.first) {
                acc.second = acc.first;
                acc.first = value;
            } else if (value > acc.second) {
                acc.second = value;
            }
        }

        // 输出(数值，排名)的二元组，输出两行数据
        public void emitValue(Top2Accumulator acc, Collector<Tuple2<Long, Integer>> out) {
            if (acc.first != Long.MIN_VALUE) {
                out.collect(Tuple2.of(acc.first, 1));
            }
            if (acc.second != Long.MIN_VALUE) {
                out.collect(Tuple2.of(acc.second, 2));
            }
        }
    }

}

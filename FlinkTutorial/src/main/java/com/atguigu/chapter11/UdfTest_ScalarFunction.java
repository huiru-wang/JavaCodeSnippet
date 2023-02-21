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
import org.apache.flink.table.functions.ScalarFunction;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class UdfTest_ScalarFunction {
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
                $("timestamp").rowtime().as("ts"));
        tableEnv.createTemporaryView("EventTable", eventTable);

        // 3. 注册自定义标量函数
        tableEnv.createTemporarySystemFunction("MyHash", MyHash.class);

        // 4. 调用UDF查询转换
        Table resultTable = tableEnv.sqlQuery("select user, MyHash(user) from EventTable");

        // 5. 输出到控制台
        tableEnv.executeSql("create table output (" +
                "uname STRING, " +
                "myhash INT ) " +
                "WITH (" +
                "'connector' = 'print')");
        resultTable.executeInsert("output");
    }

     // 自定义一个ScalarFunction
    public static class MyHash extends ScalarFunction {
        public int eval(String str){
            return str.hashCode();
        }
     }
}

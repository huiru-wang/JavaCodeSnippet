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
import org.apache.flink.table.functions.TableFunction;

import java.time.Duration;

import static org.apache.flink.table.api.Expressions.$;

public class UdfTest_TableFunction {
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

        // 3. 注册自定义表函数
        tableEnv.createTemporarySystemFunction("MySplit", MySplit.class);

        // 4. 调用UDF查询转换
        Table resultTable = tableEnv.sqlQuery("select user, url, word, length " +
                "from EventTable, LATERAL TABLE( MySplit(url) ) AS T(word, length)");

        // 5. 输出到控制台
        tableEnv.executeSql("create table output (" +
                "uname STRING, " +
                "url STRING, " +
                "word STRING, " +
                "length INT) " +
                "WITH (" +
                "'connector' = 'print')");
        resultTable.executeInsert("output");
    }

    // 自定义一个TableFunction，注意有泛型，这里输出的是两个字段，二元组
    public static class MySplit extends TableFunction<Tuple2<String, Integer>>{
        public void eval(String str){
            String[] fields = str.split("\\?");    // 转义问号，以及反斜杠本身
            for (String field : fields){
                collect(Tuple2.of(field, field.length()));
            }
        }
    }
}

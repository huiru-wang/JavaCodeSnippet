package com.atguigu.chapter11;

import com.atguigu.chapter05.Event;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

public class TableToStreamExample {
    public static void main(String[] args) throws Exception {
        // 获取流环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        // 读取数据源
        SingleOutputStreamOperator<Event> eventStream = env
                .fromElements(
                        new Event("Alice", "./home", 1000L),
                        new Event("Bob", "./cart", 1000L),
                        new Event("Alice", "./prod?id=1", 5 * 1000L),
                        new Event("Cary", "./home", 60 * 1000L),
                        new Event("Bob", "./prod?id=3", 90 * 1000L),
                        new Event("Alice", "./prod?id=7", 105 * 1000L)
                );

        // 获取表环境
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        // 将数据流转换成表
        tableEnv.createTemporaryView("EventTable", eventStream);


        // 查询Alice的访问url列表
        Table aliceVisitTable = tableEnv.sqlQuery("SELECT url, user FROM EventTable WHERE user = 'Alice'");

        // 统计每个用户的点击次数
        Table urlCountTable = tableEnv.sqlQuery("SELECT user, COUNT(url) FROM EventTable GROUP BY user");

        // 将表转换成数据流，在控制台打印输出
        tableEnv.toDataStream(aliceVisitTable).print("alice visit");
        tableEnv.toChangelogStream(urlCountTable).print("count");

        // 执行程序
        env.execute();
    }
}


package com.atguigu.chapter05;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransUdfTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Event> clicks = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L)
        );

        // 1. 传入实现FilterFunction接口的自定义函数类
        DataStream<Event> stream1 = clicks.filter(new FlinkFilter());

        // 传入属性字段
        DataStream<Event> stream2 = clicks.filter(new KeyWordFilter("home"));

        // 2. 传入匿名类
        DataStream<Event> stream3 = clicks.filter(new FilterFunction<Event>() {
            @Override
            public boolean filter(Event value) throws Exception {
                return value.url.contains("home");
            }
        });

        // 3. 传入Lambda表达式
        SingleOutputStreamOperator<Event> stream4 = clicks.filter(data -> data.url.contains("home"));

//        stream1.print();
//        stream2.print();
//        stream3.print();
        stream4.print();

        env.execute();
    }

    public static class FlinkFilter implements FilterFunction<Event> {
        @Override
        public boolean filter(Event value) throws Exception {
            return value.url.contains("home");
        }
    }

    public static class KeyWordFilter implements FilterFunction<Event> {
        private String keyWord;

        KeyWordFilter(String keyWord) { this.keyWord = keyWord; }

        @Override
        public boolean filter(Event value) throws Exception {
            return value.url.contains(this.keyWord);
        }
    }
}


package com.atguigu.chapter05;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransFilterTest {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Event> stream = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L)
        );

        // 传入匿名类实现FilterFunction
        stream.filter(new FilterFunction<Event>() {
            @Override
            public boolean filter(Event e) throws Exception {
                return e.user.equals("Mary");
            }
        });

        // 传入FilterFunction实现类
        stream.filter(new UserFilter()).print();

        env.execute();
    }
    public static class UserFilter implements FilterFunction<Event> {
        @Override
        public boolean filter(Event e) throws Exception {
            return e.user.equals("Mary");
        }
    }
}


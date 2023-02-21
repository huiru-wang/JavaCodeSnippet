package com.atguigu.chapter05;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class TransReturnTypeTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Event> clicks = env.fromElements(
                new Event("Mary", "./home", 1000L),
                new Event("Bob", "./cart", 2000L)
        );

        // 想要转换成二元组类型，需要进行以下处理
        // 1) 使用显式的 ".returns(...)"
        DataStream<Tuple2<String, Long>> stream3 = clicks
                .map( event -> Tuple2.of(event.user, 1L) )
                .returns(Types.TUPLE(Types.STRING, Types.LONG));
        stream3.print();


        // 2) 使用类来替代Lambda表达式
        clicks.map(new MyTuple2Mapper())
                .print();

        // 3) 使用匿名类来代替Lambda表达式
        clicks.map(new MapFunction<Event, Tuple2<String, Long>>() {
            @Override
            public Tuple2<String, Long> map(Event value) throws Exception {
                return Tuple2.of(value.user, 1L);
            }
        }).print();

        env.execute();
    }

    // 自定义MapFunction的实现类
    public static class MyTuple2Mapper implements MapFunction<Event, Tuple2<String, Long>>{
        @Override
        public Tuple2<String, Long> map(Event value) throws Exception {
            return Tuple2.of(value.user, 1L);
        }
    }
}


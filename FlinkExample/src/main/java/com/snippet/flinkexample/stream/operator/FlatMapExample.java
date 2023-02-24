package com.snippet.flinkexample.stream.operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.calcite.shaded.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

/**
 * 转换算子
 * <p>
 * create by whr on 2023/2/24
 */
public class FlatMapExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        DataStreamSource<ArrayList<String>> dataStreamSource = env.fromElements(
                Lists.newArrayList("item-1-1", "item-1-2"),
                Lists.newArrayList("item-2-1", "item-2-2"),
                Lists.newArrayList("item-3-1", "item-3-2")
        );

        SingleOutputStreamOperator<String> flatMapOperator = dataStreamSource.flatMap(new FlatMapFunction<ArrayList<String>, String>() {
            @Override
            public void flatMap(ArrayList<String> items, Collector<String> collector) {
                for (String item : items) {
                    collector.collect(item);
                }
            }
        });

        flatMapOperator.print();
        env.execute();
    }
}

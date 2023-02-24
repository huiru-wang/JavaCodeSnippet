package com.snippet.flinkexample.stream.operator;

import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.source.EventSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Objects;

/**
 * 转换算子
 * <p>
 * create by whr on 2023/2/24
 */
public class FilterExample {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        DataStreamSource<Event> eventDataStreamSource = env.addSource(new EventSource());

        SingleOutputStreamOperator<Event> filterResult = eventDataStreamSource
                .filter(event -> Objects.equals("Service", event.getCategory()));

        filterResult.print();

        env.execute();
    }
}

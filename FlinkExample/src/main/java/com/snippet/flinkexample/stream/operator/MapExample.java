package com.snippet.flinkexample.stream.operator;

import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.source.EventSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 转换算子
 * Object1 ---map---> Object2
 * <p>
 * create by whr on 2023/2/24
 */
public class MapExample {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Event> eventDataStreamSource = env.addSource(new EventSource());

        SingleOutputStreamOperator<String> mapResult = eventDataStreamSource.map(event -> event.getServiceId());

        mapResult.print();

        env.execute();
    }
}

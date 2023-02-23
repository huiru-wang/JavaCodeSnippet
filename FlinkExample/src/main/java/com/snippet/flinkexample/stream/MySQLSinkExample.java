package com.snippet.flinkexample.stream;

import com.snippet.flinkexample.model.MockEvent;
import com.snippet.flinkexample.source.MockEventSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * create by whr on 2023/2/22
 */
public class MySQLSinkExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        SingleOutputStreamOperator<MockEvent> streamOperator = env.addSource(new MockEventSource());

        SingleOutputStreamOperator<MockEvent> aggregateOperator = streamOperator.keyBy(MockEvent::getServiceId)
                // .flatMap()
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .sum(1);

        aggregateOperator.print();

        env.execute();
    }
}

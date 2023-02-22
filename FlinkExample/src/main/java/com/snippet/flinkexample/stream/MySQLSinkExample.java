package com.snippet.flinkexample.stream;

import com.snippet.flinkexample.model.MockEvent;
import com.snippet.flinkexample.source.MockEventSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
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

        SingleOutputStreamOperator<MockEvent> streamOperator = env.addSource(new MockEventSource())
                .assignTimestampsAndWatermarks(WatermarkStrategy.<MockEvent>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<MockEvent>() {
                            @Override
                            public long extractTimestamp(MockEvent element, long recordTimestamp) {
                                return element.getStartTime();
                            }
                        })
                );

        SingleOutputStreamOperator<MockEvent> aggregateOperator = streamOperator.keyBy(MockEvent::getServiceId)
                // .flatMap()   // TODO
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .sum(1);

        aggregateOperator.print();

        env.execute();
    }
}

package com.snippet.flinkexample.stream.watermark;

import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.source.EventSource;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * create by whr on 2023/2/24
 */
public class OrderStreamExample {

    public static void main(String[] args) {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Event> eventDataStreamSource = env.addSource(new EventSource());

        // 依据Event的发生时间，周期性生成时间戳，不考虑乱序，不设置延迟；
        //
        SingleOutputStreamOperator<Event> singleDataStream = eventDataStreamSource.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Event>forMonotonousTimestamps()
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                LocalDateTime startTime = element.getStartTime();
                                return startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                            }
                        }));


    }
}

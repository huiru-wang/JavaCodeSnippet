package com.snippet.flinkexample.stream.window;

import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.stream.trigger.TimeoutTrigger;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 每10s内的服务Id 个数统计
 * 自定义超时Trigger，时间窗内有数据，但无法触发关窗时，超时触发
 * <p>
 * create by whr on 2023/2/23
 */
public class TumbleWindowExample {

    public static void main(String[] args) throws Exception {
        // 获取流环境
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<Event> eventDataStreamSource = executionEnvironment.fromElements(
                new Event("TradeService", LocalDateTime.now().minusSeconds(9)),
                new Event("ProductService", LocalDateTime.now().minusSeconds(16)),
                new Event("TradeService", LocalDateTime.now().minusSeconds(1)),
                new Event("TradeService", LocalDateTime.now().minusSeconds(5)),
                new Event("OrderService", LocalDateTime.now().minusSeconds(1)),
                new Event("ProductService", LocalDateTime.now().minusSeconds(31)),
                new Event("TradeService", LocalDateTime.now().minusSeconds(27)),
                new Event("OrderService", LocalDateTime.now().minusSeconds(20)),
                new Event("TradeService", LocalDateTime.now().minusSeconds(10))
        );

        WatermarkStrategy<Event> timestampAssigner = WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                    @Override
                    public long extractTimestamp(Event element, long recordTimestamp) {
                        LocalDateTime startTime = element.getStartTime();
                        return startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                    }
                });

        SingleOutputStreamOperator<Tuple2<String, Long>> sum = eventDataStreamSource.assignTimestampsAndWatermarks(timestampAssigner)
                .flatMap(new FlatMapFunction<Event, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(Event value, Collector<Tuple2<String, Long>> out) throws Exception {
                        out.collect(Tuple2.of(value.getServiceId(), 1L));
                    }
                })
                .keyBy(tuple -> tuple.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .trigger(new TimeoutTrigger(Duration.ofMinutes(1))) // 无数据时，超时关闭窗口
                .sum(1);


        sum.print("sum :\n");
        // 每10s内的服务Id 个数统计


        executionEnvironment.execute();
    }
}

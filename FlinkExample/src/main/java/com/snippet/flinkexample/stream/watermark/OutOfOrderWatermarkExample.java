package com.snippet.flinkexample.stream.watermark;

import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.utils.DateUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.calcite.shaded.com.google.common.collect.Lists;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

/**
 * create by whr on 2023/2/24
 */
public class OutOfOrderWatermarkExample {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Event> eventDataStreamSource = executionEnvironment.fromElements(
                new Event("TradeService", 1L, "ALARM", "Redis", "10.34.17.8", LocalDateTime.now().minusSeconds(9)),
                new Event("ProductService", 31L, "OK", "CPU", "10.34.17.8", LocalDateTime.now().minusSeconds(16)),
                new Event("TradeService", 27L, "ALARM", "CPU", "10.34.17.1", LocalDateTime.now().minusSeconds(1)),
                new Event("TradeService", 21L, "OK", "Service", "10.34.17.8", LocalDateTime.now().minusSeconds(5)),
                new Event("OrderService", 11L, "ALARM", "MySQL", "10.34.16.8", LocalDateTime.now().minusSeconds(1)),
                new Event("ProductService", 12L, "OK", "CPU", "10.34.27.8", LocalDateTime.now().minusSeconds(31)),
                new Event("TradeService", 192L, "ALARM", "MySQL", "10.35.17.8", LocalDateTime.now().minusSeconds(27)),
                new Event("OrderService", 77L, "OK", "CPU", "10.34.17.9", LocalDateTime.now().minusSeconds(20)),
                new Event("TradeService", 88L, "ALARM", "CPU", "10.34.16.8", LocalDateTime.now().minusSeconds(10)),
                new Event("ProductService", 91L, "OK", "Redis", "10.33.17.8", LocalDateTime.now())
        );

        // 乱序事件时间，需指定水位线的延迟时间
        SingleOutputStreamOperator<Event> singleDataStream = eventDataStreamSource.assignTimestampsAndWatermarks(
                WatermarkStrategy.<Event>forBoundedOutOfOrderness(Duration.ofSeconds(10))
                        .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                            @Override
                            public long extractTimestamp(Event element, long recordTimestamp) {
                                LocalDateTime startTime = element.getStartTime();
                                return startTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                            }
                        })
        );

        SingleOutputStreamOperator<String> result = singleDataStream
                .map(new MapFunction<Event, Tuple2<Event, Long>>() {
                    @Override
                    public Tuple2<Event, Long> map(Event value) throws Exception {
                        return Tuple2.of(value, 1L);
                    }
                }).keyBy(tuple -> tuple.f0.getServiceId())
                .window(TumblingEventTimeWindows.of(Time.seconds(10)))
                .apply(new WindowFunction<Tuple2<Event, Long>, String, String, TimeWindow>() {
                    @Override
                    public void apply(String serviceId, TimeWindow window, Iterable<Tuple2<Event, Long>> input, Collector<String> out) throws Exception {
                        ArrayList<Tuple2<Event, Long>> tuple2s = Lists.newArrayList(input);
                        if (tuple2s.isEmpty()) {
                            return;
                        }
                        long start = window.getStart();
                        String dateStr = DateUtils.timestampToLocalDate(start);
                        StringBuilder alertIds = new StringBuilder();
                        StringBuilder hostIps = new StringBuilder();
                        for (Tuple2<Event, Long> item : tuple2s) {
                            Event event = item.f0;
                            Long alertId = event.getAlertId();
                            String hostIp = event.getHostIp();
                            alertIds.append(alertId).append(",");
                            hostIps.append(hostIp).append(",");
                        }
                        alertIds.deleteCharAt(alertIds.length() - 1);
                        hostIps.deleteCharAt(hostIps.length() - 1);
                        String res = String.format("[%s] ServiceId:[%s] | Num:[%d] | AlertIds:[%s] | hostIps:[%s] ", dateStr, serviceId, tuple2s.size(), alertIds, hostIps);
                        out.collect(res);
                    }
                });
        // TODO 聚合

        result.print();
//        6> [2023-02-24 23:18:00] ServiceId:[TradeService]   | Num:[1] | AlertIds:[192]      | hostIps:[10.35.17.8]
//        6> [2023-02-24 23:18:20] ServiceId:[TradeService]   | Num:[3] | AlertIds:[88,21,1]  | hostIps:[10.34.16.8,10.34.17.8,10.34.17.8]
//        3> [2023-02-24 23:18:10] ServiceId:[OrderService]   | Num:[1] | AlertIds:[77]       |  hostIps:[10.34.17.9]
//        6> [2023-02-24 23:18:30] ServiceId:[TradeService]   | Num:[1] | AlertIds:[27]       | hostIps:[10.34.17.1]
//        3> [2023-02-24 23:18:10] ServiceId:[ProductService] | Num:[1] | AlertIds:[31]       | hostIps:[10.34.17.8]
//        3> [2023-02-24 23:18:30] ServiceId:[OrderService]   | Num:[1] | AlertIds:[11]       | hostIps:[10.34.16.8]
//        3> [2023-02-24 23:18:30] ServiceId:[ProductService] | Num:[1] | AlertIds:[91]       | hostIps:[10.33.17.8]
        executionEnvironment.execute();
    }
}

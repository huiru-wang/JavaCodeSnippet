package com.atguigu.chapter07;

/**
 * Copyright (c) 2020-2030 尚硅谷 All Rights Reserved
 * <p>
 * Project:  FlinkTutorial
 * <p>
 * Created by  wushengran
 */

import com.atguigu.chapter05.ClickSource;
import com.atguigu.chapter05.Event;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ProcessAllWindowTopN {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        SingleOutputStreamOperator<Event> eventStream = env.addSource(new ClickSource())
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Event>forMonotonousTimestamps()
                                .withTimestampAssigner(new SerializableTimestampAssigner<Event>() {
                                    @Override
                                    public long extractTimestamp(Event element, long recordTimestamp) {
                                        return element.timestamp;
                                    }
                                })
                );

        // 只需要url就可以统计数量，所以转换成String直接开窗统计
        SingleOutputStreamOperator<String> result = eventStream
                .map(new MapFunction<Event, String>() {
                    @Override
                    public String map(Event value) throws Exception {
                        return value.url;
                    }
                })
                .windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))    // 开滑动窗口
                .process(new ProcessAllWindowFunction<String, String, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<String> elements, Collector<String> out) throws Exception {
                        HashMap<String, Long> urlCountMap = new HashMap<>();
                        // 遍历窗口中数据，将浏览量保存到一个 HashMap 中
                        for (String url : elements) {
                            if (urlCountMap.containsKey(url)) {
                                long count = urlCountMap.get(url);
                                urlCountMap.put(url, count + 1L);
                            } else {
                                urlCountMap.put(url, 1L);
                            }
                        }
                        ArrayList<Tuple2<String, Long>> mapList = new ArrayList<Tuple2<String, Long>>();
                        // 将浏览量数据放入ArrayList，进行排序
                        for (String key : urlCountMap.keySet()) {
                            mapList.add(Tuple2.of(key, urlCountMap.get(key)));
                        }
                        mapList.sort(new Comparator<Tuple2<String, Long>>() {
                            @Override
                            public int compare(Tuple2<String, Long> o1, Tuple2<String, Long> o2) {
                                return o2.f1.intValue() - o1.f1.intValue();
                            }
                        });
                        // 取排序后的前两名，构建输出结果
                        StringBuilder result = new StringBuilder();
                        result.append("========================================\n");
                        for (int i = 0; i < 2; i++) {
                            Tuple2<String, Long> temp = mapList.get(i);
                            String info = "浏览量No." + (i + 1) +
                                    " url：" + temp.f0 +
                                    " 浏览量：" + temp.f1 +
                                    " 窗口结束时间：" + new Timestamp(context.window().getEnd()) + "\n";

                            result.append(info);
                        }
                        result.append("========================================\n");
                        out.collect(result.toString());
                    }
                });

        result.print();

        env.execute();
    }
}


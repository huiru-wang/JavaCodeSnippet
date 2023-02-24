package com.snippet.flinkexample.stream.operator;

import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.source.EventSource;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 聚合算子：数据统计<p>
 * keyBy：聚合操作的前提，对数据流进行分区，归类(进行 逻辑分区) <p>
 * 按照选中key的哈希值进行分区；并非真正的物理分区，因为分区有限，key的值可能很多 <p>
 * keyBy后可接多种聚合函数，对同一类的流进行聚合操作； <p>
 * - sum <p>
 * - max: 只招指定的字段的max，其他字段可能不是同一条数据 <p>
 * - maxBy：选出指定字段最大的那一条数据 <p>
 * - min <p>
 * <p>
 * create by whr on 2023/2/24
 */
public class KeyByExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(4);
        DataStreamSource<Event> eventDataStreamSource = env.addSource(new EventSource());

        SingleOutputStreamOperator<Tuple2<String, Long>> countData = eventDataStreamSource
                .flatMap(new FlatMapFunction<Event, Tuple2<String, Long>>() {
                    @Override
                    public void flatMap(Event event, Collector<Tuple2<String, Long>> collector) {
                        collector.collect(Tuple2.of(event.getServiceId(), 1L));
                    }
                })
                .keyBy(event -> event.f0)
                .sum(1);

        countData.print();

        env.execute();
    }
}

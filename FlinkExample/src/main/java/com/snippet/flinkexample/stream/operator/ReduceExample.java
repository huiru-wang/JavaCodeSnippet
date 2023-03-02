package com.snippet.flinkexample.stream.operator;

import com.snippet.flinkexample.model.Event;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;

/**
 * reduce：归约算子，流中两两元素不断迭代成新的元素，类型保持相同
 * create by whr on 2023/2/24
 */
public class ReduceExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        DataStreamSource<Event> dataStreamSource = env.fromElements(
                new Event("TradeService", LocalDateTime.now().minusSeconds(9)),
                new Event("ProductService", LocalDateTime.now().minusSeconds(16)),
                new Event("TradeService", LocalDateTime.now().minusSeconds(1)),
                new Event("TradeService", LocalDateTime.now().minusSeconds(5)),
                new Event("OrderService", LocalDateTime.now().minusSeconds(1)),
                new Event("ProductService", LocalDateTime.now().minusSeconds(31)),
                new Event("OrderService", LocalDateTime.now().minusSeconds(20)),
                new Event("TradeService", LocalDateTime.now().minusSeconds(10))
        );

        SingleOutputStreamOperator<Tuple2<Event, Long>> reduceResult = dataStreamSource
                .flatMap(new FlatMapFunction<Event, Tuple2<Event, Long>>() {
                    @Override
                    public void flatMap(Event value, Collector<Tuple2<Event, Long>> out) {
                        out.collect(Tuple2.of(value, 1L));
                    }
                })
                .setParallelism(3)
                .keyBy(tuple -> tuple.f0.getServiceId())
                .reduce(new ReduceFunction<Tuple2<Event, Long>>() {
                    @Override
                    public Tuple2<Event, Long> reduce(Tuple2<Event, Long> value1, Tuple2<Event, Long> value2) {
                        return Tuple2.of(value1.f0, value1.f1 + value2.f1);
                    }
                });

        reduceResult.print();
//        1> (Event(serviceId=ProductService),1)
//        2> (Event(serviceId=TradeService),1)
//        2> (Event(serviceId=TradeService),2)
//        2> (Event(serviceId=TradeService),3)
//        2> (Event(serviceId=OrderService),1)
        reduceResult.keyBy(tuple -> tuple.f0).maxBy(1).print();
        // (k1,10)

        env.execute();
    }
}

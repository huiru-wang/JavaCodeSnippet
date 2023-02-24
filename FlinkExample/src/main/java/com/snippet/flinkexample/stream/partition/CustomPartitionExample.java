package com.snippet.flinkexample.stream.partition;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * create by whr on 2023/2/24
 */
public class CustomPartitionExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        DataStreamSource<Tuple2<String, Long>> dataStreamSource = env.fromElements(
                Tuple2.of("1", 1L),
                Tuple2.of("1", 5L),
                Tuple2.of("1", 4L),
                Tuple2.of("2", 1L),
                Tuple2.of("2", 2L),
                Tuple2.of("2", 3L),
                Tuple2.of("3", 1L),
                Tuple2.of("3", 5L),
                Tuple2.of("3", 4L),
                Tuple2.of("4", 1L),
                Tuple2.of("4", 2L),
                Tuple2.of("4", 3L)
        );

        dataStreamSource.partitionCustom(new Partitioner<Integer>() {
            @Override
            public int partition(Integer key, int numPartitions) {
                return (key - 1) % numPartitions;
            }
        }, new KeySelector<Tuple2<String, Long>, Integer>() {
            @Override
            public Integer getKey(Tuple2<String, Long> value) throws Exception {
                return Integer.parseInt(value.f0);
            }
        }).print("partitionCustom: \n").setParallelism(4);
        // 4> (4,1)
        // 3> (3,1)
        // 1> (1,1)
        // 2> (2,1)
        
        env.execute();
    }
}

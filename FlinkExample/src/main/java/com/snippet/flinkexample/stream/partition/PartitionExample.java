package com.snippet.flinkexample.stream.partition;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 分区场景，当由少数分区，向多数分区扩散，分配的策略
 * 1、默认轮询
 * 2、shuffle 均匀随机
 * 3、rescale
 * 4、广播方式：不能算是分区，每个元素都广播到所有分区
 * 5、自定义分区
 * <p>
 * create by whr on 2023/2/24
 */
public class PartitionExample {

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

        SingleOutputStreamOperator<Tuple2<String, Long>> partitionStream = dataStreamSource.keyBy(tuple -> tuple.f0)
                .map(new MapFunction<Tuple2<String, Long>, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(Tuple2<String, Long> value) throws Exception {
                        return Tuple2.of(value.f0, value.f1 + 1L);
                    }
                });
        // keyBy后分区为2, 使用分区函数，重新分到4个分区

        // shuffle分区：随机k1,k2可能出现在4个分区的任何一个；
        partitionStream.shuffle().print("shuffle: \n").setParallelism(4);


        // k1 和 k2 将分别会占有2个分区，不会出现 k1出现在各个分区中
        partitionStream.rescale().print("Rescale: \n").setParallelism(4);


        // 每个元素都复制到所有分区
        partitionStream.broadcast().print("broadcast: \n").setParallelism(4);

        // 自定义分区
        partitionStream.partitionCustom(new Partitioner<Integer>() {
            @Override
            public int partition(Integer key, int numPartitions) {
                return key % 4;
            }
        }, new KeySelector<Tuple2<String, Long>, Integer>() {
            @Override
            public Integer getKey(Tuple2<String, Long> value) throws Exception {
                return null;
            }
        }).print().setParallelism(4);

        env.execute();
    }
}

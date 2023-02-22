package com.snippet.flinkexample.stream;

import com.snippet.flinkexample.constant.Constants;
import com.snippet.flinkexample.model.KafkaMessageModel;
import com.snippet.flinkexample.serialization.MessageDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.Duration;

/**
 * create by whr on 2023/2/22
 */
public class KafkaSourceExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        KafkaSource<KafkaMessageModel> kafkaSource = KafkaSource.<KafkaMessageModel>builder()
                .setBootstrapServers(Constants.KAFKA_SERVERS)
                .setTopics(Constants.KAFKA_TOPIC)
                .setGroupId("flink_stream")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setDeserializer(new MessageDeserializationSchema())
                //.setValueOnlyDeserializer(new MessageDeserializationSchema())   // 自定义消息体序列化
                .setProperty("partition.discovery.interval.ms", "10000")    // 每10s 动态发现partition
                // .setProperty("security.protocol", "SASL_PLAINTEXT")
                .build();

        // TODO checkpoint?

        DataStreamSource<KafkaMessageModel> dataStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(5)),
                "kafka Source"
        );

        dataStream.print();

        env.execute();
    }
}

package com.snippet.flinkexample.stream.sink;

import com.alibaba.fastjson2.JSON;
import com.snippet.flinkexample.constant.Constants;
import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.source.EventSource;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * create by whr on 2023/2/22
 */
public class KafkaSinkExample {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Event> mockEventDataStreamSource = env.addSource(new EventSource());

        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers(Constants.KAFKA_SERVERS)
                .setRecordSerializer(
                        KafkaRecordSerializationSchema.builder()
                                .setTopic(Constants.KAFKA_TOPIC)
                                .setKeySerializationSchema(new SimpleStringSchema())
                                .setValueSerializationSchema(new SimpleStringSchema())
                                .build())
                .setDeliverGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();

        SingleOutputStreamOperator<String> stringSingleOutputStreamOperator = mockEventDataStreamSource
                .flatMap(new FlatMapFunction<Event, String>() {
                    @Override
                    public void flatMap(Event value, Collector<String> out) throws Exception {
                        out.collect(JSON.toJSONString(value));
                    }
                });

        stringSingleOutputStreamOperator.sinkTo(kafkaSink);

        env.execute();
    }
}

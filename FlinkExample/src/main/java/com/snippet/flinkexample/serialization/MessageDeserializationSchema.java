package com.snippet.flinkexample.serialization;

import com.alibaba.fastjson2.JSON;
import com.snippet.flinkexample.model.Event;
import com.snippet.flinkexample.model.KafkaMessageModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * kafka 反序列化
 * create by whr on 2023/2/22
 */
@Slf4j
public class MessageDeserializationSchema implements KafkaRecordDeserializationSchema<KafkaMessageModel> {

    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> record, Collector<KafkaMessageModel> out) throws IOException {
        if (Objects.isNull(record) || Objects.isNull(record.value())) {
            return;
        }
        String key = null;
        String value = null;
        KafkaMessageModel messageModel = new KafkaMessageModel();
        try {
            if (Objects.nonNull(record.key())) {
                key = new String(record.key(), StandardCharsets.UTF_8);
                messageModel.setKey(key);
            }
            value = new String(record.value(), StandardCharsets.UTF_8);
            Event event = JSON.parseObject(value, Event.class);
            messageModel.setValue(event);
            messageModel.setValue(JSON.parseObject(record.value(), Event.class));
            messageModel.setOffset(record.offset());
            out.collect(messageModel);
        } catch (Exception e) {
            log.error("JSON parse failed: key:{}, value:{}", key, value);
        }
    }

    @Override
    public TypeInformation<KafkaMessageModel> getProducedType() {
        return TypeInformation.of(KafkaMessageModel.class);
    }
}

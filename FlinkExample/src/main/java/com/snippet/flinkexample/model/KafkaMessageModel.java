package com.snippet.flinkexample.model;

import lombok.Data;

/**
 * create by whr on 2023/2/22
 */
@Data
public class KafkaMessageModel {

    private String key;

    private Event value;

    private Long offset;

    public KafkaMessageModel() {
    }

    public KafkaMessageModel(String key, Event value, Long offset) {
        this.key = key;
        this.value = value;
        this.offset = offset;
    }
}

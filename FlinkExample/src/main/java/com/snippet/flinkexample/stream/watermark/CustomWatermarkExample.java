package com.snippet.flinkexample.stream.watermark;

import cn.hutool.core.util.IdUtil;
import com.snippet.flinkexample.model.Event;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.time.LocalDateTime;

/**
 * create by whr on 2023/2/24
 */
public class CustomWatermarkExample {

    public static void main(String[] args) {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        executionEnvironment.fromElements(
                new Event("TradeService", IdUtil.getSnowflakeNextId(), "OK", "CPU", "10.34.17.8", LocalDateTime.now()));
    }
}

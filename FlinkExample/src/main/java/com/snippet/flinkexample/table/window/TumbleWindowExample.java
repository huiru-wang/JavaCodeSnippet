package com.snippet.flinkexample.table.window;

import com.snippet.flinkexample.udf.LocalDateTimeParserFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * create by whr on 2023/2/23
 */
public class TumbleWindowExample {
    public static void main(String[] args) throws Exception {
        // 获取流环境
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(executionEnvironment);

        tableEnv.createTemporaryFunction("ParseTimestamp", new LocalDateTimeParserFunction());
        String kafkaSource = "CREATE TABLE fault_event (\n" +
                "  serviceId STRING,\n" +
                "  microServiceId STRING,\n" +
                "  category STRING,\n" +
                "  alertId BIGINT,\n" +
                "  opType STRING,\n" +
                "  level STRING,\n" +
                "  hostIp STRING,\n" +
                "  startTime TIMESTAMP\n" +
                ") WITH (\n" +
                " 'connector' = 'kafka',\n" +
                " 'topic' = 'snippet',\n" +
                " 'properties.bootstrap.servers' = 'localhost:9092',\n" +
                " 'properties.group.id' = 'flink_table',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n" +
                " 'value.format' = 'json'\n" +
                ")";
        // kafka source
        tableEnv.executeSql(kafkaSource);

        String sql = "SELECT CAST(TUMBLE_START(ts, INTERVAL '5' SECOND) AS STRING) window_start, COUNT(serviceId) \n" +
                " FROM fault_event \n" +
                " GROUP BY TUMBLE(ts, INTERVAL '5' SECOND)";

        tableEnv.sqlQuery(sql);

        executionEnvironment.execute();
    }

}

package com.snippet.flinkexample.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * create by whr on 2023/2/22
 */
public class KafkaToMySQLExample {
    public static void main(String[] args) throws Exception {
        // 获取流环境
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(executionEnvironment);

        String kafkaSource = "CREATE TABLE fault_event (\n" +
                "  serviceId STRING,\n" +
                "  microServiceId STRING,\n" +
                "  category STRING,\n" +
                "  alertId BIGINT,\n" +
                "  opType STRING,\n" +
                "  level STRING,\n" +
                "  hostIp STRING,\n" +
                "  startTime TIMESTAMP,\n" +
                "  WATERMARK FOR startTime AS startTime - INTERVAL '5' SECOND " +
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


        String ddlSink = "CREATE TABLE public_alert_agg (\n" +
                "  startTime TIMESTAMP(3),\n" +
                "  serviceNum BIGINT\n" +
                ") WITH (\n" +
                " 'connector' = 'jdbc',\n" +
                " 'driver' = 'com.mysql.cj.jdbc.Driver',\n" +
                " 'url' = 'jdbc:mysql://localhost:3306/snippet',\n" +
                " 'table-name' = 't_public_alert_agg', \n" +
                " 'username' = 'root', \n" +
                " 'password' = 'root',\n" +
                " 'sink.buffer-flush.max-rows' = '100', \n" +
                " 'sink.buffer-flush.interval' = '600'\n" +
                ")";

        tableEnv.executeSql(ddlSink);

        String sql = "INSERT INTO public_alert_agg SELECT window_start AS startTime, COUNT(serviceId) AS serviceNum \n" +
                " FROM TABLE ( TUMBLE( TABLE fault_event, DESCRIPTOR(ts), INTERVAL '10' SECONDS) ) \n" +
                " GROUP BY window_start, window_end";

        StatementSet stmtSet = tableEnv.createStatementSet();
        stmtSet.addInsertSql(sql);
        stmtSet.execute();

    }
}

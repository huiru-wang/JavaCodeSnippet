package com.snippet.flinkexample.constant;

/**
 * create by whr on 2023/2/22
 */
public class DDL {
    public static final String ALERT_AGG_TABLE_NAME = "alertAggTable";

    public static final String ALERT_AGG_TABLE = "CREATE TABLE alertAggTable (\n" +
            "  id BIGINT,\n" +
            "  startTime BIGINT,\n" +
            "  serviceNum BIGINT,\n" +
            "  alertNum BIGINT,\n" +
            "  hostIpNum BIGINT\n" +
            ") WITH (\n" +
            "   'connector' = 'jdbc',\n" +
            "   'url' = 'jdbc:mysql://" + Constants.SERVER_HOST + ":3306/snippet',\n" +
            "   'username' = 'root',\n" +
            "   'password' = 'root',\n" +
            "   'table-name' = 't_alert_agg'\n" +
            ")";

    public static final String KAFKA_MESSAGE_TABLE_NAME = "mockEventTable";

    public static final String KAFKA_MESSAGE_TABLE = "CREATE TABLE mockEventTable (\n" +
            "  topic STRING METADATA VIRTUAL,\n" +
            "  event_key STRING,\n" +
            "  offset BIGINT METADATA VIRTUAL,\n" +
            "  event_time TIMESTAMP(3) METADATA FROM 'timestamp',\n" +    // Kafka 记录的时间戳 重命名
            "  serviceId STRING,\n" +
            "  alertId BIGINT,\n" +
            "  status STRING,\n" +
            "  alertLevel STRING,\n" +
            "  hostIp STRING,\n" +
            "  startTime BIGINT,\n" +
            "  ts AS TO_TIMESTAMP( FROM_UNIXTIME(startTime / 1000) ),\n" +
            ") WITH (\n" +
            " 'connector' = 'kafka',\n" +
            " 'topic' = 'snippet',\n" +
            " 'properties.bootstrap.servers' = '" + Constants.SERVER_HOST + "',\n" +
            " 'properties.group.id' = 'flink_table',\n" +
            " 'key.field' = 'event_key',\n" +
            " 'value.format' = 'json',\n" +
            " 'json.fail-on-missing-field' = 'false',\n" +
            " 'json.ignore-parse-errors' = 'true'\n" +
            ")";

}

public class DDL {
    public static final String FAULT_AGG_TABLE_NAME = "faultAggTable";
    public static final String FAULT_AGG_TABLE = "CREATE TABLE publicAlertAggTable (\n" +
            "  startTime BIGINT,\n" +
            "  serviceNum BIGINT,\n" +
            "  alertNum BIGINT,\n" +
            "  hostIpNum BIGINT\n" +
            ") WITH (\n" +
            "   'connector' = 'jdbc',\n" +
            "   'url' = 'jdbc:mysql://localhost:3306/snippet',\n" +
            "   'username' = 'root',\n" +
            "   'password' = 'root',\n" +
            "   'table-name' = 't_public_alert_agg,'\n" +
            "   '' = ''\n" +
            ")";
    public static final String KAFKA_MESSAGE_TABLE_NAME = "faultEventTable";

    public static final String KAFKA_MESSAGE_TABLE = "CREATE TABLE faultEventTable (\n" +
            "  topic STRING METADATA VIRTUAL,\n" +
            "  event_key STRING,\n" +
            "  offset BIGINT METADATA VIRTUAL,\n" +
            "  event_time TIMESTAMP(3) METADATA FROM 'timestamp',\n" +    // Kafka 记录的时间戳 重命名
            "  serviceId STRING,\n" +
            "  microServiceId STRING,\n" +
            "  category STRING,\n" +
            "  alertId BIGINT,\n" +
            "  opType STRING,\n" +
            "  level STRING,\n" +
            "  startTimestamp BIGINT,\n" +
            "  ts BIGINT,\n" +
            ") WITH (\n" +
            " 'connector' = 'kafka',\n" +
            " 'topic' = 'snippet',\n" +
            " 'properties.bootstrap.servers' = 'localhost:9092',\n" +
            " 'properties.group.id' = 'flink_table',\n" +
            " 'key.field' = 'event_key',\n"+
            " 'value.format' = 'json',\n" +
            " 'json.fail-on-missing-field' = 'false',\n" +
            " 'json.ignore-parse-errors' = 'true'\n" +
            ")";

}

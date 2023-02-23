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
                "  hostIp STRING,\n"+
                "  startTimestamp BIGINT,\n" +
                "  ts AS TO_TIMESTAMP( FROM_UNIXTIME(startTimestamp / 1000) ),\n"+
                "  WATERMARK FOR ts AS ts - INTERVAL '5' SECOND "+
                ") WITH (\n" +
                " 'connector' = 'kafka',\n" +
                " 'topic' = 'snippet',\n" +
                " 'properties.bootstrap.servers' = 'localhost:9092',\n" +
                " 'properties.group.id' = 'flink_table',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n"+
                " 'value.format' = 'json'\n" +
                ")";
        // kafka source
        tableEnv.executeSql(kafkaSource);

        String sql = "SELECT CAST(TUMBLE_START(ts, INTERVAL '5' SECOND) AS STRING) window_start, COUNT(serviceId) \n" +
                " FROM fault_event \n" +
                " GROUP BY TUMBLE(ts, INTERVAL '5' SECOND)";

        Table table = tableEnv.sqlQuery(sql);

        tableEnv.toDataStream(table, Row.class).print();

        executionEnvironment.execute();
    }

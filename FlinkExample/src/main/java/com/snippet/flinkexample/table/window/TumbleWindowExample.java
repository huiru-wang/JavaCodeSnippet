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


        String ddlSink = "CREATE TABLE public_alert_agg (\n" +
                "  startTime BIGINT\n" +
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

        String sql = "insert into public_alert_agg select startTimestamp from fault_event";

        tableEnv.executeSql(sql);

        executionEnvironment.execute();
    }

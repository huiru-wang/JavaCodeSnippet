package com.snippet.flinkexample.stream.window;

/**
 * create by whr on 2023/2/23
 */
public class TumbleWindowExample {
  
      public static void main(String[] args) throws Exception {
        // 获取流环境
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(executionEnvironment);

        tableEnv.createTemporaryFunction("ParseLocalDate", new LocalDateTimeParserFunction());

        String kafkaSource = "CREATE TABLE fault_event (\n" +
                "  id BIGINT,\n"+
                "  site STRING,\n"+
                "  tenantId STRING,\n"+
                "  applicationId STRING,\n"+
                "  provider STRING,\n"+
                "  serviceId STRING,\n" +
                "  subType STRING,\n" +
                "  status STRING,\n" +
                "  objName STRING,\n" +
                "  msg STRING,\n"+
                "  isSuppressed BOOLEAN,\n"+
                "  isDeleted BOOLEAN,\n"+
                "  hostIp STRING,\n"+
                "  occurTime STRING,\n" +
                "  detectTime STRING,\n" +
                "  lastModifiedTime STRING,\n" +
                "  ts AS TO_TIMESTAMP( ParseLocalDate(occurTime) ),\n"+
                "  WATERMARK FOR ts AS ts - INTERVAL '60' SECOND "+
                ") WITH (\n" +
                " 'connector' = 'kafka',\n" +
                " 'topic' = 'snippet1',\n" +
                " 'properties.bootstrap.servers' = 'localhost:9092',\n" +
                " 'properties.group.id' = 'flink_table',\n" +
                " 'scan.startup.mode' = 'earliest-offset',\n"+
                " 'value.format' = 'json'\n" +
                ")";
        // kafka source
        tableEnv.executeSql(kafkaSource);

        Table selectTable = tableEnv.sqlQuery("SELECT \n" +
                " ts,id,site,tenantId,applicationId,serviceId,provider,subType,status,msg, \n" +
                " isSuppressed,isDeleted,occurTime,detectTime,lastModifiedTime,hostIp \n" +
                " FROM fault_event");
        selectTable.printSchema();
        selectTable.execute().print();


        Table tempTable = tableEnv.sqlQuery("SELECT  window_start,\n" +
                " site,tenantId,applicationId,serviceId,subType,COUNT(hostIp) AS hostIpNum,COUNT(id) AS alertNum \n" +
                " FROM TABLE(TUMBLE(TABLE fault_event, DESCRIPTOR(ts), INTERVAL '1' SECOND)) " +
                " GROUP BY window_start,window_end,site, tenantId, applicationId, serviceId,subType"
        );

        tempTable.printSchema();
        tableEnv.toDataStream(tempTable).print();
        tempTable.execute().print();
        tableEnv.createTemporaryView("TempTable", tempTable);

        String mysqlSink = "CREATE TABLE public_alert_agg (\n" +
                "  startTime TIMESTAMP(3),\n" +
                "  serviceNum BIGINT,\n" +
                "  alertNum BIGINT,\n" +
                "  hostIpNum BIGINT\n" +
                ") WITH (\n" +
                " 'connector' = 'jdbc',\n" +
                " 'driver' = 'com.mysql.cj.jdbc.Driver',\n" +
                " 'url' = 'jdbc:mysql://localhost:3306/snippet',\n" +
                " 'table-name' = 't_public_alert_agg', \n" +
                " 'username' = 'root', \n" +
                " 'password' = 'root',\n" +
                " 'sink.buffer-flush.max-rows' = '100', \n" +
                " 'sink.buffer-flush.interval' = '10000'\n" +
                ")";

        tableEnv.executeSql(mysqlSink);

        executionEnvironment.execute();
    }
}

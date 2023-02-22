public class MySQLBatchSinkExample {

    public static void main(String[] args) throws Exception {
        // 获取流环境
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(executionEnvironment, settings);

        // 读取数据源
        SingleOutputStreamOperator<FaultAggModel> eventStream = executionEnvironment
                .fromElements(
                        new FaultAggModel(IdUtil.getSnowflakeNextId(),System.currentTimeMillis(),4L,2L,1L),
                        new FaultAggModel(IdUtil.getSnowflakeNextId(),System.currentTimeMillis(),4L,2L,1L),
                        new FaultAggModel(IdUtil.getSnowflakeNextId(),System.currentTimeMillis(),4L,2L,1L),
                        new FaultAggModel(IdUtil.getSnowflakeNextId(),System.currentTimeMillis(),4L,2L,1L),
                        new FaultAggModel(IdUtil.getSnowflakeNextId(),System.currentTimeMillis(),4L,2L,1L)
                );
        Table sourceTable = tableEnv.fromDataStream(eventStream);

        tableEnv.executeSql(DDL.FAULT_AGG_TABLE);

        String sql = "INSERT INTO "+ DDL.FAULT_AGG_TABLE_NAME +" SELECT id, startTime,alertNum,serviceNum,hostIpNum FROM " + sourceTable;

        // 批量插
        StatementSet stmtSet = tableEnv.createStatementSet();
        stmtSet.addInsertSql(sql);

        TableResult insertResult = stmtSet.execute();

        executionEnvironment.execute();
    }
}

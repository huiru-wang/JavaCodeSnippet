package com.snippet.flinkexample.table;

import cn.hutool.core.util.IdUtil;
import com.snippet.flinkexample.constant.DDL;
import com.snippet.flinkexample.model.AlertAggModel;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * create by whr on 2023/2/22
 */
public class MySQLBatchInsertExample {
    public static void main(String[] args) throws Exception {
        // 获取流环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<AlertAggModel> streamSource = env.fromElements(
                new AlertAggModel(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
                new AlertAggModel(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
                new AlertAggModel(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
                new AlertAggModel(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
                new AlertAggModel(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L)
        );

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        tableEnv.createTemporaryView("EventTable", streamSource);

        // 读取数据源
//        Table table = tableEnv.fromValues(
//                row(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
//                row(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
//                row(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
//                row(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L),
//                row(IdUtil.getSnowflakeNextId(), System.currentTimeMillis(), 4L, 2L, 1L)
//        );

        tableEnv.executeSql(DDL.ALERT_AGG_TABLE);

        String sql = "INSERT INTO " + DDL.ALERT_AGG_TABLE_NAME +
                " SELECT id,startTime,serviceNum,alertNum,hostIpNum FROM EventTable";

        // 批量插
        StatementSet stmtSet = tableEnv.createStatementSet();
        stmtSet.addInsertSql(sql);

        TableResult insertResult = stmtSet.execute();

        env.execute();
    }
}

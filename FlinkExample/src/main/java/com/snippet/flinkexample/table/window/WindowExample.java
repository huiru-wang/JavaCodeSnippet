package com.snippet.flinkexample.table.window;

import com.snippet.flinkexample.udf.LocalDateTimeParserFunction;
import com.snippet.flinkexample.udf.TimestampParserFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * create by whr on 2023/2/23
 */
public class WindowExample {
    public static void main(String[] args) throws Exception {
        // set up execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);

        tEnv.createTemporaryFunction("ParseLocalDate", new LocalDateTimeParserFunction());

        tEnv.createTemporaryFunction("ParseTimestamp", new TimestampParserFunction());

        // write source data into temporary file and get the absolute path
        String contents =
                "1,beer,3,2019-12-12T00:00:01.123\n" +
                        "1,diaper,4,2019-12-12T00:00:02.345\n" +
                        "2,pen,3,2019-12-12T00:00:04.132\n" +
                        "2,rubber,3,2019-12-12T00:00:26.132\n" +
                        "3,rubber,2,2019-12-12T00:00:35.132\n" +
                        "4,beer,1,2019-12-12T00:00:18.132";
        String path = createTempFile(contents);

        String ddl = "CREATE TABLE orders (\n" +
                "  user_id INT,\n" +
                "  product STRING,\n" +
                "  amount INT,\n" +
                "  startTime STRING,\n" +
                "  ts AS TO_TIMESTAMP(ParseLocalDate(startTime)), \n" +
                "  WATERMARK FOR ts AS ts - INTERVAL '3' SECOND\n" +
                ") WITH (\n" +
                "  'connector.type' = 'filesystem',\n" +
                "  'connector.path' = '" + path + "',\n" +
                "  'format.type' = 'csv'\n" +
                ")";
        tEnv.executeSql(ddl);

        tEnv.executeSql("SELECT user_id,product,amount, ts FROM orders ").print();
        String query = "SELECT\n" +
                "  CAST(TUMBLE_START(ts, INTERVAL '5' SECOND) AS STRING) window_start,\n" +
                "  COUNT(*) order_num,\n" +
                "  SUM(amount) total_amount,\n" +
                "  COUNT(DISTINCT product) unique_products\n" +
                "FROM orders\n" +
                "GROUP BY TUMBLE(ts, INTERVAL '5' SECOND)";
        tEnv.executeSql(query).print();

    }

    private static String createTempFile(String contents) throws IOException {
        File tempFile = File.createTempFile("orders", ".csv");
        tempFile.deleteOnExit();
        FileUtils.writeFileUtf8(tempFile, contents);
        return tempFile.toURI().toString();
    }
}

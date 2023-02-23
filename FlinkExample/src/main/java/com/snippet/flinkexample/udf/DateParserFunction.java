package com.snippet.flinkexample.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * create by whr on 2023/2/23
 */
public class DateParserFunction extends ScalarFunction {

    private static final String DEFAULT_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public Timestamp eval(String dateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
        LocalDateTime localDate = LocalDateTime.parse(dateStr, dateTimeFormatter);
        return Timestamp.valueOf(localDate);
    }
}

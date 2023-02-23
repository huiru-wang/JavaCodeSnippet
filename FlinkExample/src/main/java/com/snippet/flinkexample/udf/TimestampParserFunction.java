package com.snippet.flinkexample.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * create by whr on 2023/2/24
 */
public class TimestampParserFunction extends ScalarFunction {

    private static final String DEFAULT_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public Long eval(String dateStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        //return Timestamp.valueOf(localDate);
    }
}

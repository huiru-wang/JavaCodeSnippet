package com.snippet.flinkexample.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * create by whr on 2023/2/23
 */
public class LocalDateTimeParserFunction extends ScalarFunction {

    private static final String DEFAULT_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final String NORMAL_FMT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
    private static final DateTimeFormatter normalFormatter = DateTimeFormatter.ofPattern(NORMAL_FMT);

    public String eval(String dateStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, defaultFormatter);
        return localDateTime.format(normalFormatter);
    }
}

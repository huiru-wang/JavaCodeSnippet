package com.snippet.flinkexample.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * create by whr on 2023/2/24
 */
public class DateUtils {

    public static final String MILLSECOND_FMT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String DEFAULT_FMT = "yyyy-MM-dd HH:mm:ss";

    public static final String GMT8_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static String timestampToLocalDate(Long timestamp) {
        if (Objects.isNull(timestamp)) {
            return "";
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        return format(localDateTime);
    }

    /**
     * 时间戳转 时间字符串
     *
     * @param timestamp 时间戳
     * @return 时间字符串
     */
    public static String timestampToDateStr(Long timestamp) {
        if (Objects.isNull(timestamp)) {
            return "";
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return format(localDateTime);
    }

    public static String format(LocalDateTime localDateTime, String fmt) {
        if (StringUtils.isBlank(fmt)) {
            return format(localDateTime);
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
        return localDateTime.format(dateTimeFormatter);
    }

    public static String format(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
        return localDateTime.format(dateTimeFormatter);
    }
}

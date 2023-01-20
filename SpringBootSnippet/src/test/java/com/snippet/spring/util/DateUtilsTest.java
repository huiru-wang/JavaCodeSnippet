package com.snippet.spring.util;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtilsTest {
    public static final String DEFAULT_FMT = "yyyy-MM-dd HH:mm:ss";

    /**
     * LocalDateTime 简单使用
     */
    @Test
    public void localDateTime_usage() {
        // 当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        // 字符串 转 LocalDateTime
        String dateStr = "2023-01-20 09:01:09";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
        LocalDateTime localDate = LocalDateTime.parse(dateStr, dateTimeFormatter);

        // LocalDateTime转字符串
        String localDateStr = localDate.toString();
        System.out.println(localDateStr);

        // LocalDateTime获取时间戳
        Instant instant = localDate.toInstant(ZoneOffset.ofHours(8));
        long timestamp = instant.toEpochMilli();
        System.out.println(timestamp);

        // 本地时间转UTC时间
        // LocalDateTime创建后不记录时区
        // 必须以当前时区，将LocalDateTime转成ZonedDateTime, 在进行UTC转换
        ZonedDateTime defaultZoneTime = localDate.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = defaultZoneTime.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println(utc.toString());
        System.out.println(utc.toLocalTime());

    }

    @Test
    public void dateStr_to_utcDateStr() {
        String localDateStr = "2023-01-20 09:01:09";
        String utcDateStr = DateUtils.getUtcDateStr(localDateStr);
        Assert.equals(utcDateStr, "2023-01-20 01:01:09");

    }
}

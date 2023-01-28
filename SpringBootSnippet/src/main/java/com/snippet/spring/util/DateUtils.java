package com.snippet.spring.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 * <br/>
 * 推荐使用：<br/>
 * - 本地时间类：LocalDateTime<br/>
 * - 格式化时间类：DateTimeFormatter<br/>
 * <br/>
 * 与MySql时间相对应：datetime <--> LocalDateTime
 * <br/>
 * SimpleDateFormat线程不安全，不推荐使用
 */
public class DateUtils {
    public static final String MILLSECOND_FMT = "yyyy-MM-dd HH:mm:ss.SSSSSS";
    public static final String DEFAULT_FMT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 转utc时间字符串
     *
     * @param dateStr 时间字符串:yyyy-MM-dd HH:mm:ss
     * @return utc时间字符串
     */
    public static String getUtcDateStr(@NonNull String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
        LocalDateTime localDate = LocalDateTime.parse(dateStr, dateTimeFormatter);
        ZonedDateTime defaultZoneTime = localDate.atZone(ZoneId.systemDefault());
        ZonedDateTime utcDateTime = defaultZoneTime.withZoneSameInstant(ZoneId.of("UTC"));
        String utcDateTimeStr = utcDateTime.toString();
        utcDateTimeStr.replaceAll("[^0-9:-]", " ").trim(); // 替换掉非数字:-
    }
}

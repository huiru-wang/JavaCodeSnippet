package com.snippet.spring.util;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        Date localDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FMT);
        try {
            localDate = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            String msg = String.format("parse date str fail, dateStr:%s", dateStr);
            throw new IllegalArgumentException("illegal date format: " + msg);
        }
        long time = localDate.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        Date utcDate = new Date(calendar.getTimeInMillis());

        return simpleDateFormat.format(utcDate);
    }

}

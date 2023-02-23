package com.snippet;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * create by whr on 2023/2/22
 */
public class SimpleTest {

    @Test
    public void mock_event_json() {
        System.out.println(LocalDateTime.now());
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        System.out.println(timestamp.toString());
    }

    private static final String DEFAULT_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    @Test
    public void local_time() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_FMT);
        String localTime = now.format(dateTimeFormatter);
        System.out.println(localTime);

        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        ZonedDateTime utc = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println(utc.toString());

        Timestamp timestamp = Timestamp.from(utc.toInstant());
        System.out.println(timestamp);
    }
}

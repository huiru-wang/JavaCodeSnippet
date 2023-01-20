package com.snippet.spring.util;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

    @Test
    public void dateStr_to_utcDateStr() {
        String localDateStr = "2023-01-20 9:01:09";
        String utcDateStr = DateUtils.getUtcDateStr(localDateStr);
        Assert.equals(utcDateStr, "2023-01-20 01:01:09");

    }
}

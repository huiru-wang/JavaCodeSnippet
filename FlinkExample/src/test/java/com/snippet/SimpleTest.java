package com.snippet;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.snippet.flinkexample.model.MockEvent;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * create by whr on 2023/2/22
 */
public class SimpleTest {
    String[] serviceList = new String[]{"UserService", "OrderInsight", "ProductService"};

    String[] statusList = new String[]{"resolved", "firing"};

    String[] alertLevelList = new String[]{"notice", "warning", "minor", "major", "critical"};

    String[] hostIpList = new String[]{"10.23.12.4",
            "10.23.12.5", "10.23.12.6", "10.23.12.7", "10.23.12.8", "10.23.12.9", "10.23.13.4", "10.23.14.4"};

    @Test
    public void mock_event_json() {
        Random random = new Random();
        MockEvent mockEvent = new MockEvent(
                serviceList[random.nextInt(serviceList.length)],
                IdUtil.getSnowflakeNextId(),
                statusList[random.nextInt(statusList.length)],
                alertLevelList[random.nextInt(alertLevelList.length)],
                hostIpList[random.nextInt(hostIpList.length)],
                System.currentTimeMillis());
        String jsonString = JSON.toJSONString(mockEvent);
        System.out.println(jsonString);
    }
}

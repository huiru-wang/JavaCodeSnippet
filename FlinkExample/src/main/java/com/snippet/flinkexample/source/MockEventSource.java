package com.snippet.flinkexample.source;

import cn.hutool.core.util.IdUtil;
import com.snippet.flinkexample.model.MockEvent;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * create by whr on 2023/2/22
 */
public class MockEventSource implements SourceFunction<MockEvent> {
    private Boolean running = true;

    String[] serviceList = new String[]{"UserService", "OrderInsight", "ProductService"};

    String[] statusList = new String[]{"resolved", "firing"};

    String[] alertLevelList = new String[]{"notice", "warning", "minor", "major", "critical"};

    String[] hostIpList = new String[]{"10.23.12.4",
            "10.23.12.5", "10.23.12.6", "10.23.12.7", "10.23.12.8", "10.23.12.9", "10.23.13.4", "10.23.14.4"};

    @Override
    public void run(SourceFunction.SourceContext<MockEvent> sourceContext) throws Exception {
        Random random = new Random();
        while (running) {
            sourceContext.collect(new MockEvent(
                    serviceList[random.nextInt(serviceList.length)],
                    IdUtil.getSnowflakeNextId(),
                    statusList[random.nextInt(statusList.length)],
                    alertLevelList[random.nextInt(alertLevelList.length)],
                    hostIpList[random.nextInt(hostIpList.length)],
                    LocalDateTime.now()
            ));
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}


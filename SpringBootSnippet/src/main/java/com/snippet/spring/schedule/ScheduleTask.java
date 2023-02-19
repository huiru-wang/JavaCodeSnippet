package com.snippet.spring.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Slf4j
// @Component
public class ScheduleTask {

    @Scheduled(fixedRate = 20000, initialDelay = 0L, timeUnit = TimeUnit.MILLISECONDS)
    public void run() {
        log.info("schedule task start");
    }
}

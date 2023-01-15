package com.snippet.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时任务指定线程池
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        int core = Runtime.getRuntime().availableProcessors();

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(core);
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(core);
        taskExecutor.setMaxPoolSize(core * 10);
        taskExecutor.setKeepAliveSeconds(10);
        taskExecutor.setThreadGroupName("schedule-poll");
        taskExecutor.setThreadNamePrefix("schedule-");
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.initialize();

        taskRegistrar.setScheduler(scheduledExecutorService);
    }
}

package com.snippet.flinkexample.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * create by whr on 2023/2/22
 */
@Data
public class MockEvent {

    private String serviceId;

    private Long alertId;

    private String status;

    private String alertLevel;

    private String hostIp;

    private LocalDateTime startTime;

    public MockEvent() {
    }

    public MockEvent(String serviceId, Long alertId, String status, String alertLevel, String hostIp, LocalDateTime startTime) {
        this.serviceId = serviceId;
        this.alertId = alertId;
        this.status = status;
        this.alertLevel = alertLevel;
        this.hostIp = hostIp;
        this.startTime = startTime;
    }
}

package com.snippet.flinkexample.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * create by whr on 2023/2/22
 */
@Data
public class Event {

    private String serviceId;

    private Long alertId;

    private String status;

    private String category;

    private String hostIp;

    private LocalDateTime startTime;

    public Event() {
    }

    public Event(String serviceId, Long alertId, String status, String category, String hostIp, LocalDateTime startTime) {
        this.serviceId = serviceId;
        this.alertId = alertId;
        this.status = status;
        this.category = category;
        this.hostIp = hostIp;
        this.startTime = startTime;
    }

    public Event(String serviceId, LocalDateTime startTime) {
        this.serviceId = serviceId;
        this.startTime = startTime;
    }

}

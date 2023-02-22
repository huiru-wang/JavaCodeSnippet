package com.snippet.flinkexample.model;

import lombok.Data;

/**
 * create by whr on 2023/2/22
 */
@Data
public class AlertAggModel {

    private Long id;
    private Long startTime;
    private Long serviceNum;

    private Long alertNum;

    private Long hostIpNum;


    public AlertAggModel() {
    }

    public AlertAggModel(Long id, Long serviceNum, Long alertNum, Long hostIpNum, Long startTime) {
        this.id = id;
        this.serviceNum = serviceNum;
        this.alertNum = alertNum;
        this.hostIpNum = hostIpNum;
        this.startTime = startTime;
    }
}

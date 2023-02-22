package com.snippet.flinkexample.constant;

/**
 * create by whr on 2023/2/22
 */
public class DML {

    public static final String DROP_KAFKA_TABLE = "DROP TABLE IF EXIST " + DDL.KAFKA_MESSAGE_TABLE_NAME;

    public static final String DROP_ALERT_ADD_TABLE = "DROP TABLE IF EXIST " + DDL.ALERT_AGG_TABLE_NAME;

    public static final String INSERT_TO_ALERT_AGG = "INSERT INTO t_alert_agg (id, serviceNum, hostIpNum, startTime) values (?, ?, ?, ?)";
}

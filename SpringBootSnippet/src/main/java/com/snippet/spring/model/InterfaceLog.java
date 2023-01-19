package com.snippet.spring.model;

import com.alibaba.fastjson.JSON;
import com.snippet.spring.common.constants.Constants;
import lombok.Builder;
import lombok.Data;
import org.slf4j.MDC;

/**
 * 接口日志
 */
@Data
@Builder
public class InterfaceLog {

    private String traceId;
    private String ip;

    private String interfaceName;
    private String path;

    private String httpMethod;

    private long timeCost;
    private Object request;

    private Object response;

    @Override
    public String toString() {
        StringBuilder interfaceLog = new StringBuilder();
        interfaceLog.append(Constants.Separator.VERTICAL_LINE)
                .append(MDC.get(Constants.TRACE_ID))
                .append(Constants.Separator.VERTICAL_LINE)
                .append(this.ip)
                .append(Constants.Separator.VERTICAL_LINE)
                .append(this.httpMethod)
                .append(Constants.Separator.VERTICAL_LINE)
                .append(this.path)
                .append(Constants.Separator.VERTICAL_LINE)
                .append(this.interfaceName)
                .append(Constants.Separator.VERTICAL_LINE)
                .append(this.timeCost)
                .append(Constants.Separator.VERTICAL_LINE)
                .append(JSON.toJSONString(this.request))
                .append(Constants.Separator.VERTICAL_LINE)
                .append(JSON.toJSONString(this.response));
        return interfaceLog.toString();
    }
}

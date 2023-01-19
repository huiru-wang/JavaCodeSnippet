package com.snippet.spring.interceptor;

import cn.hutool.core.lang.UUID;
import com.snippet.spring.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 增加服务间的 trace id 追踪全链路日志
 */
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(Constants.X_TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            MDC.put(Constants.TRACE_ID, UUID.randomUUID().toString(true));
        } else {
            MDC.put(Constants.TRACE_ID, traceId);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("post handler");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}

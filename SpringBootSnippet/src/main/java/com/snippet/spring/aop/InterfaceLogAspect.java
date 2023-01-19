package com.snippet.spring.aop;

import com.snippet.spring.model.InterfaceLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 对所有controller进行切面拦截 记录接口日志
 */
@Slf4j
@Aspect
@Component
public class InterfaceLogAspect {

    @Pointcut("execution(public * com.snippet.spring.controller.*.*(..))")
    public void interfaceLog() {
    }

    @Around("interfaceLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Object response = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String interfaceMethod = signature.getMethod().getName();
        String uri = request.getRequestURI();
        String ip = request.getRemoteUser();
        String httpMethod = request.getMethod();
        Object requestBody = null;
        if ("GET".equals(httpMethod)) {
            requestBody = request.getParameterMap();
        } else if ("POST".equals(httpMethod)) {
            requestBody = joinPoint.getArgs();
        }
        InterfaceLog interfaceLog = InterfaceLog.builder()
                .ip(ip)
                .path(uri)
                .interfaceName(interfaceMethod)
                .httpMethod(httpMethod)
                .timeCost(endTime - startTime)
                .request(requestBody)
                .response(response)
                .build();
        log.info("{}", interfaceLog.toString());
        return response;
    }
}

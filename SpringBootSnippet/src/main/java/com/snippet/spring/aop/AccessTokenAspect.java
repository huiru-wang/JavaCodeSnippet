package com.snippet.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AccessTokenAspect {

    @Pointcut("@annotation(com.snippet.spring.aop.annotation.AccessToken)")
    public void pointcut() {
    }

    /**
     * 使用HuTool的jwt实现
     */
    @Before("pointcut()")
    public void authenticate(JoinPoint joinPoint) {
        // 获取token

        // 校验token
        // JWTUtil.verify()
        // 验证用户信息

    }
}

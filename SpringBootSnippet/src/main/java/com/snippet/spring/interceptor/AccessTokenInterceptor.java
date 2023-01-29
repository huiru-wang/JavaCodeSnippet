package com.snippet.spring.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessTokenInterceptor implements HandlerInterceptor {
    @Value("${jwt.private.key}")
    private String jwtPrivateKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // TODO jwt
        String authorization = request.getHeader("Authorization");
        Assert.notNull(authorization, "Authorization is null");
        String[] value = authorization.split(" ");
        Assert.isTrue(value.length > 1, "Invalid Authorization");
        String token = value[1];
        boolean res = JWTUtil.verify(token, jwtSecret.getBytes());
        if (!res) {
            log.warn("Verify JWT fail");
            return false;
        }
        JWT jwt = JWTUtil.parseToken(token);
        JWTPayload payload = jwt.getPayload();
        Long userId = (Long) payload.getClaim("userId");
        // verify user

        return true;
    }
}

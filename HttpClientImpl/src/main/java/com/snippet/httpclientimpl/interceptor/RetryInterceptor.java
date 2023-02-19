package com.snippet.httpclientimpl.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * create by whr on 2023/2/19
 */
public class RetryInterceptor implements Interceptor {

    private static final Logger log = LoggerFactory.getLogger(RetryInterceptor.class);

    private int maxRetry = 3;

    private int delay = 500;

    public RetryInterceptor(int maxRetry, int delay) {
        this.maxRetry = maxRetry;
        this.delay = delay;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        int times = maxRetry;
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && times > 0) {
            times--;
            response.close();
            log.warn("response closed, remaining retry times : {}", times);
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            response = chain.proceed(request);
        }
        return response;
    }
}

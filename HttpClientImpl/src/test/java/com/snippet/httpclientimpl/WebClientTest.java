package com.snippet.httpclientimpl;

import cn.hutool.core.util.IdUtil;
import com.snippet.httpclientimpl.model.BaseResponse;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CountDownLatch;

/**
 * create by whr on 2023/2/19
 */
public class WebClientTest {
    private static final Logger log = LoggerFactory.getLogger(WebClientTest.class);

    @Test
    public void simple_sync_get() {
        WebClient webClient = WebClient.create("http://localhost:9090/api/hello/get");
        String result = webClient
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("HttpResponse:{}", result);
    }


    @Test
    public void simple_sync_post() {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", "will");

        WebClient webClient = WebClient.create();
        String result = webClient
                .post()
                .uri("http://localhost:9090/api/hello/post")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("HttpResponse:{}", result);
    }

    @Test
    public void simple_async_web_client() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        WebClient webClient = WebClient.create("http://localhost:9090/api/hello/get");

        webClient.get()
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .subscribe(response -> {
                    log.info("response: code:{}, message:{}, data:{}", response.getCode(), response.getMessage(), response.getData());   // 处理response
                    latch.countDown(); // 等待异步处理完成
                });

        latch.await();
    }

    @Test
    public void custom_attributes() {
        WebClient webClient = WebClient.create("http://localhost:9090/api/hello/badResponse");

        String result = webClient.get()
                .header("x-trace-id", IdUtil.fastSimpleUUID())
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, "auth");
                    // ..
                })
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))    // 响应不是此type，抛出WebClientResponseException$NotAcceptable
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.BAD_REQUEST)) {
                        return response.bodyToMono(String.class);
                    } else {
                        return response.createException().flatMap(Mono::error);
                    }
                })
                .block();
        log.info("HttpResponse: {}", result);
    }

    @Test
    public void handle_error_response() {
        WebClient webClient = WebClient.create("http://localhost:9090/api/hello/badResponse");

        String result = webClient
                .get()
                .retrieve()
                .onStatus(HttpStatus::isError,
                        response -> response.bodyToMono(String.class).map(e -> new RuntimeException())
                )// 当isError，处理response，并返回Mono
                .bodyToMono(String.class).blockOptional().orElse("bad request");

        log.info("responseEntity:{}", result);
    }
}

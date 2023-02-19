package com.snippet.httpclientimpl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.URLEncodeUtil;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * create by whr on 2023/2/19
 */
public class OkHttpClientTest {
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(50, 10000, TimeUnit.MILLISECONDS))
            .connectTimeout(Duration.ofMillis(2000))
            .readTimeout(Duration.ofMillis(2000))
            .retryOnConnectionFailure(true)
            .build();
    String getUrl = "http://localhost:9090/api/get";

    String postUrl = "http://localhost:9090/api/post";

    Headers headers = new Headers.Builder().set("Content-Type", "application/json").build();


    @Test
    public void sync_get() {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", "daD");
        param.put("id", "1");
        String httpUrl = parseHttpUrl(getUrl, param);

        Request request = new Request.Builder().url(httpUrl).headers(headers).get().build();
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                ResponseBody body = response.body();
                System.out.println(body.string());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    private static String parseHttpUrl(String url, Map<String, String> pathParam) {
        Assert.notNull(url, "url is null");
        HttpUrl httpUrl = HttpUrl.parse(url);
        HttpUrl.Builder builder = httpUrl.newBuilder();
        if (MapUtil.isNotEmpty(pathParam)) {
            pathParam.forEach(builder::addQueryParameter);
        }

        return URLEncodeUtil.encode(builder.build().toString());
    }

    @Test
    public void async_get() throws InterruptedException {
        Request request = new Request.Builder().url(getUrl).headers(headers).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody body = response.body();
                System.out.println(body.string());
            }
        });
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void sync_post_with_form() {
        FormBody formBody = new FormBody.Builder().add("username", "dad").build();

        Request postRequest = new Request.Builder().url("http://localhost:9090/hello/post")
                .headers(headers)
                .addHeader("Authorization", "I'm God")
                .post(formBody)
                .build();

        Call call = okHttpClient.newCall(postRequest);

        try (Response response = call.execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                ResponseBody body = response.body();
                System.out.println(body.string());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    @Test
    public void sync_post_with_json() {
        HashMap<String, String> body = new HashMap<>();
        body.put("username", "daD");

        RequestBody requestBody = RequestBody.create(JSON.toJSONString(body), MediaType.parse("application/json"));

        Request postRequest = new Request.Builder().url(postUrl)
                .headers(headers)
                .addHeader("Authorization", "I'm God")
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(postRequest);

        try (Response response = call.execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                ResponseBody responseBody = response.body();
                System.out.println(responseBody.string());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}


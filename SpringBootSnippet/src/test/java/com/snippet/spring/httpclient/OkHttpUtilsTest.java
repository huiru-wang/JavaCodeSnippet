package com.snippet.spring.httpclient;
  
import com.alibaba.fastjson.JSON;

import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * reference: https://www.baeldung.com/okhttp-post
 * 
 * @author w00623538
 * @since 2023-02-17
 */
public class OkHttpUtilsTest {
    private static final OkHttpClient okHttpClient;

    static TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }
    }};

    static {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .connectionPool(new ConnectionPool(50, 10000, TimeUnit.MILLISECONDS))
                .connectTimeout(Duration.ofMillis(2000))
                .readTimeout(Duration.ofMillis(2000))
                .retryOnConnectionFailure(true)
                .build();

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    String url = "http://localhost:9090/hello/get";

    Headers headers = new Headers.Builder().set("Content-Type", "application/json").build();

    @Test
    public void sync_get() {
        HashMap<String, String> param = new HashMap<>();
        param.put("username", "daD");
        param.put("id", "1");
        HttpUrl httpUrl = parseHttpUrl(url, param);

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

    private HttpUrl parseHttpUrl(String url, Map<String, String> pathParam) {
        HttpUrl.Builder builder = new HttpUrl.Builder().parse$okhttp(null, url);
        pathParam.forEach(builder::addQueryParameter);
        return builder.build();
    }

    @Test
    public void async_get() throws InterruptedException {
        Request request = new Request.Builder().url(url).headers(headers).get().build();
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

        Request postRequest = new Request.Builder().url("http://localhost:9090/hello/post")
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

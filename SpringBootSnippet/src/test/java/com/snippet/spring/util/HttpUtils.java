
import cn.hutool.core.map.MapUtil;
import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
    }

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

    public static Response get(String url, Map<String, String> pathParam, Map<String, String> headersMap) {
        Request request = getRequest(url, HttpMethod.GET, headersMap, pathParam, null);
        return execute(request);
    }

    public static Response post(String url, Map<String, String> headersMap, String requestBody) {
        Request request = getRequest(url, HttpMethod.GET, headersMap, null, requestBody);
        return execute(request);
    }

    public static void asyncGet(String url, Map<String, String> pathParam, Map<String, String> headersMap,
        Consumer<Response> consumer) {
        Request request = getRequest(url, HttpMethod.GET, headersMap, pathParam, null);
        asyncExecute(request, callback(consumer));
    }

    public static void asyncPost(String url, Map<String, String> headersMap, String requestBody,
        Consumer<Response> consumer) {
        Request request = getRequest(url, HttpMethod.GET, headersMap, null, requestBody);
        asyncExecute(request, callback(consumer));
    }

    private static Response execute(Request request) {
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            return response;
        } catch (IOException e) {
            log.error("IOException: ", e);
        }
        return null;
    }

    private static void asyncExecute(Request request, Callback callback) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    private static Callback callback(Consumer<Response> consumer) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                consumer.accept(response);
            }
        };
    }

    private static Request getRequest(String url, HttpMethod httpMethod, Map<String, String> headersMap,
        Map<String, String> pathParam, String body) {
        Request.Builder builder = new Request.Builder();
        if (MapUtil.isNotEmpty(headersMap)) {
            builder.headers(parseHeaders(headersMap));
        }
        if (HttpMethod.GET.equals(httpMethod)) {
            if (MapUtil.isNotEmpty(pathParam)) {
                HttpUrl httpUrl = parseHttpUrl(url, pathParam);
                builder.url(httpUrl).get();
            } else {
                builder.url(url).get();
            }
        } else if (HttpMethod.POST.equals(httpMethod)) {
            RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));
            builder.url(url).post(requestBody);
        }
        return builder.build();
    }

    private static HttpUrl parseHttpUrl(String url, Map<String, String> pathParam) {
        HttpUrl.Builder builder = new HttpUrl.Builder().parse$okhttp(null, url);
        pathParam.forEach(builder::addQueryParameter);
        return builder.build();
    }

    private static Headers parseHeaders(Map<String, String> headers) {
        Headers.Builder builder = new Headers.Builder();
        headers.forEach(builder::set);
        return builder.build();
    }
}

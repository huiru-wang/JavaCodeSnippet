
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import com.huawei.cp.wiseeye.common.model.response.WiseEyeResponse;
import com.huawei.cp.wiseeye.common.model.response.code.WiseEyeError;
import com.huawei.cp.wiseeye.faultlifecycle.common.constant.EnvConstant;
import com.huawei.cp.wiseeye.faultlifecycle.common.interceptor.RetryInterceptor;
import okhttp3.*;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @since 2023-02-17
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {
    }

    private static final OkHttpClient okHttpClient;

    static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
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
                    .connectTimeout(Duration.ofMillis(EnvConstant.connectTimeout))
                    .readTimeout(Duration.ofMillis(EnvConstant.readTimeout))
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new RetryInterceptor(3, 500))
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String baseUrl, Map<String, String> pathParam, Map<String, String> headersMap) {
        Request request = getRequest(baseUrl, HttpMethod.GET, headersMap, pathParam, null);
        return execute(request);
    }

    public static String post(String baseUrl, Map<String, String> headersMap, String requestBody) {
        Request request = getRequest(baseUrl, HttpMethod.POST, headersMap, null, requestBody);
        return execute(request);
    }

    public static void asyncGet(String url, Map<String, String> pathParam, Map<String, String> headersMap,
                                BiConsumer<Response, IOException> consumer) {
        Request request = getRequest(url, HttpMethod.GET, headersMap, pathParam, null);
        asyncExecute(request, callback(consumer));
    }

    public static void asyncPost(String url, Map<String, String> headersMap, String requestBody,
                                 BiConsumer<Response, IOException> consumer) {
        Request request = getRequest(url, HttpMethod.POST, headersMap, null, requestBody);
        asyncExecute(request, callback(consumer));
    }

    private static Response execute(Request request) {
        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("IOException: ", e);
        }
        return null;
    }

    private static void asyncExecute(Request request, Callback callback) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    private static Callback callback(BiConsumer<Response, IOException> consumer) {
        return new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                consumer.accept(null, e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                consumer.accept(response, null);
            }
        };
    }

    private static Request getRequest(String url, HttpMethod httpMethod, Map<String, String> headersMap,
                                      Map<String, String> pathParam, String body) {
        Request.Builder builder = new Request.Builder();
        if (MapUtil.isNotEmpty(headersMap)) {
            builder.headers(parseHeaders(headersMap));
        }
        String encodedUrl = parseHttpUrl(url, pathParam);
        if (HttpMethod.GET.equals(httpMethod)) {
            builder.url(encodedUrl).get();
        } else if (HttpMethod.POST.equals(httpMethod)) {
            RequestBody requestBody = null;
            if (StrUtil.isBlank(body)) {
                requestBody = RequestBody.create(null, "");
            } else {
                requestBody = RequestBody.create(body, MediaType.parse("application/json"));
            }
            builder.url(encodedUrl).post(requestBody);
        }
        return builder.build();
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

    private static Headers parseHeaders(Map<String, String> headers) {
        Headers.Builder builder = new Headers.Builder();
        if (MapUtil.isNotEmpty(headers)) {
            headers.forEach(builder::set);
        }
        // TODO 添加其他header
        return builder.set("Authorization", "Authorization").build();
    }
}

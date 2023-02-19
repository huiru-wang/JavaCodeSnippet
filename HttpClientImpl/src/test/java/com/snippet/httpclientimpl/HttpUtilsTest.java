package com.snippet.httpclientimpl;

import com.snippet.httpclientimpl.utils.HttpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * create by whr on 2023/2/19
 */
public class HttpUtilsTest {
    String getUrl = "http://localhost:9090/api/get";

    String postUrl = "http://localhost:9090/api/post";
    String retryUrl = "http://localhost:9090/api/retry";

    @Test
    public void get_test() {
        String response = HttpUtils.get(getUrl, null, null);
        Assert.isTrue("get response".equals(response), "fail");
    }

    @Test
    public void post_test() {
        String response = HttpUtils.post(postUrl, null, null);
        Assert.isTrue("post response".equals(response), "fail");
    }

    @Test
    public void async_get_test() throws InterruptedException {
        HttpUtils.asyncGet(getUrl, null, null, (r, e) -> {
            if (Objects.isNull(e)) {
                try {
                    System.out.println(r.body().string());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println(e);
            }
        });
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void async_post_test() throws InterruptedException {
        HttpUtils.asyncPost(postUrl, null, null, (r, e) -> {
            if (Objects.isNull(e)) {
                try {
                    System.out.println(r.body().string());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println(e);
            }
        });
        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void retry_test() {
        String response = HttpUtils.post(retryUrl, null, null);
        Assert.isTrue("retry response".equals(response), "fail");
    }
}

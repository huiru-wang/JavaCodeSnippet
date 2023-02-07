package com.snippet.spring.config;

import cn.hutool.core.util.IdUtil;
import com.snippet.spring.common.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * create by whr on 2023/2/7
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().defaultHeader(Constants.X_TRACE_ID, IdUtil.fastSimpleUUID()).build();
    }


}

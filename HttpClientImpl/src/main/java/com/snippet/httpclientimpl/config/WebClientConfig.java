package com.snippet.httpclientimpl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

/**
 * create by whr on 2023/2/19
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("snippet")
                .pendingAcquireTimeout(Duration.ofMillis(2000))
                .maxConnections(100)
                .pendingAcquireMaxCount(10000)
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider);

        ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .clientConnector(reactorClientHttpConnector)
                // .defaultHeader(Constants.X_TRACE_ID, IdUtil.fastSimpleUUID())
                .build();
    }
}

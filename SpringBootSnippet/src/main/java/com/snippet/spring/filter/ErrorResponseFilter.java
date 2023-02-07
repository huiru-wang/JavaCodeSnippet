package com.snippet.spring.filter;

import com.snippet.spring.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

/**
 * create by whr on 2023/2/8
 */
public class ErrorResponseFilter {

    public static Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatus status = response.statusCode();
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new BusinessException(-1, body)));
        }
        if (HttpStatus.BAD_REQUEST.equals(status)) {
            return response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new BusinessException(-1, body)));
        }
        return Mono.just(response);
    }

}

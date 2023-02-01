package com.snippet.spring.model.mq;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class OrderMessage {

    @NotBlank
    private String orderId;

    @NotBlank
    private String userId;

    private String orderInfo;
}

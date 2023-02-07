package com.snippet.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class BaseResponse<T> {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse() {
    }
}

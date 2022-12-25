package com.snippet.spring.common;

import lombok.Getter;

@Getter
public enum ResponseEnums {

    SUCCESS(0, "success"),
    PARAM_INVALID(-1, "param invalid");

    private final Integer code;

    private final String message;

    ResponseEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

package com.snippet.spring.common.enums;

import lombok.Getter;

@Getter
public enum ResponseEnums {

    SUCCESS(0, "success"),
    PARAM_INVALID(10001, "param invalid"),

    // 用户相关
    USER_INFO_UPDATE_FAIL(20001, "user info update fail"),
    ;

    private final Integer code;

    private final String message;

    ResponseEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

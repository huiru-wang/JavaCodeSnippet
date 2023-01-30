package com.snippet.spring.common.enums;

import lombok.Getter;
/**
* 错误码：模块 | 编号 | 类型
*/
@Getter
public enum ResponseEnums {

    SUCCESS(0, "success"),
    PARAM_INVALID(10001, "param invalid"),
    AUTHENTICATION_FAIL(10010, "authentication fail"),
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

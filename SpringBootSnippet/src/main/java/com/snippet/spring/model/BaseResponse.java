package com.snippet.spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snippet.spring.common.enums.ResponseEnums;
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

    public static <T> BaseResponse<T> fail(int code, String message, T data) {
        return new BaseResponse<>(code, message, data);
    }

    public static <T> BaseResponse<T> ok() {
        return new BaseResponse<>(ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getMessage(), null);
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getMessage(), data);
    }

    public static BaseResponse fail(int code, String message) {
        return new BaseResponse<>(code, message, null);
    }

    public static <T> BaseResponse<T> fail(ResponseEnums responseEnums) {
        return new BaseResponse<>(responseEnums.getCode(), responseEnums.getMessage(), null);
    }

    public static <T> BaseResponse<T> fail(ResponseEnums responseEnums, T data) {
        return new BaseResponse<>(responseEnums.getCode(), responseEnums.getMessage(), data);
    }
}

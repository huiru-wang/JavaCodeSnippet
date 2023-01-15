package com.snippet.spring.util;

import com.snippet.spring.common.ResponseEnums;
import com.snippet.spring.model.BaseResponse;

public class ResponseUtil {

    public static <T> BaseResponse<T> fail(int code, String message, T data) {
        return new BaseResponse<>(code, message, data);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getMessage(), data);
    }

    public static BaseResponse fail(ResponseEnums responseEnums) {
        return new BaseResponse<>(responseEnums.getCode(), responseEnums.getMessage(), null);
    }

    public static <T> BaseResponse<T> fail(ResponseEnums responseEnums, T data) {
        return new BaseResponse<>(responseEnums.getCode(), responseEnums.getMessage(), data);
    }
}

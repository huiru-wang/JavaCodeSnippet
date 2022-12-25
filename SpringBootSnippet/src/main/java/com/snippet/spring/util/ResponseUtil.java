package com.snippet.spring.util;

import com.snippet.spring.common.ResponseEnums;
import com.snippet.spring.model.BaseResponse;

public class ResponseUtil {

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseEnums.SUCCESS, data);
    }

    public static BaseResponse fail(ResponseEnums responseEnums) {
        return new BaseResponse<>(responseEnums);
    }
}

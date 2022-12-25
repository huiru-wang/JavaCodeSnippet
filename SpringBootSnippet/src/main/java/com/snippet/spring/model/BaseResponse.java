package com.snippet.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.snippet.spring.common.ResponseEnums;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    public BaseResponse(ResponseEnums responseEnums) {
        this.code = responseEnums.getCode();
        this.message = responseEnums.getMessage();
    }

    public BaseResponse(ResponseEnums responseEnums, T data) {
        this.code = responseEnums.getCode();
        this.message = responseEnums.getMessage();
        this.data = data;
    }

}

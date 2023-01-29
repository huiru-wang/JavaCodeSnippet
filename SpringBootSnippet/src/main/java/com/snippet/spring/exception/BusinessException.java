package com.snippet.spring.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(ResponseEnums responseEnums) {
        super(responseEnums.getMessage());
        this.code = responseEnums.getCode();
    }
}

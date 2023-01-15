package com.snippet.spring.exception;

import com.snippet.spring.common.ResponseEnums;
import com.snippet.spring.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.error("IllegalArgument: ", e);
        int code = ResponseEnums.PARAM_INVALID.getCode();
        String message = ResponseEnums.PARAM_INVALID.getMessage() + ": " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(code, message, null));
    }
}

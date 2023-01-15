package com.snippet.spring.exception;

import com.snippet.spring.common.ResponseEnums;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.util.ResponseUtil;
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
    public ResponseEntity<BaseResponse> handleIllegalArgument(IllegalArgumentException e) {
        log.error("IllegalArgument: ", e);
        String message = ResponseEnums.PARAM_INVALID.getMessage() + ": " + e.getMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ResponseUtil.fail(ResponseEnums.PARAM_INVALID.getCode(), message, null));
    }
}

package com.snippet.spring.exception;

import com.snippet.spring.common.enums.ResponseEnums;
import com.snippet.spring.model.BaseResponse;
import com.snippet.spring.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
/**
 * 异常流顺序：
 * 方法抛出ex --> aop@AfterThrowing可处理异常 --> @RestControllerAdvice --> interceptor.afterCompletion()方法处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public BaseResponse handleBusinessException(BusinessException e) {

        return ResponseUtil.fail(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理参数异常
     *
     * @param e IllegalArgumentException
     * @return BaseResponse
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    public BaseResponse handleIllegalArgument(Exception e) {
        log.error("IllegalArgument: ", e);
        StringBuilder errorMessage = new StringBuilder(ResponseEnums.PARAM_INVALID.getMessage());
        errorMessage.append(": ");
        if (e instanceof MissingServletRequestParameterException) {

            // lombok 校验异常
            MissingServletRequestParameterException ex = (MissingServletRequestParameterException) e;
            String parameterName = ex.getParameterName();
            errorMessage.append(parameterName);

        } else if (e instanceof IllegalArgumentException) {

            // Assert 校验异常
            IllegalArgumentException ex = (IllegalArgumentException) e;
            String message = ex.getMessage();
            errorMessage.append(message);

        } else if (e instanceof MethodArgumentNotValidException) {

            // @Validate注解异常
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = ex.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.forEach(error -> {
                FieldError fieldError = (FieldError) error;
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errorMessage.append("[").append(field).append("]: ").append(message);
            });

        }
        int code = ResponseEnums.PARAM_INVALID.getCode();
        return ResponseUtil.fail(code, errorMessage.toString());
    }
}

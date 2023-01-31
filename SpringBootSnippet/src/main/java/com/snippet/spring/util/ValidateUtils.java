package com.snippet.spring.util;


import javax.validation.*;
import java.util.Set;

public class ValidateUtils {

    private static final Validator validator;
    static {
         validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> void validateWithException(T obj){
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (constraintViolations.isEmpty()) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        constraintViolations.forEach(constraintViolation->{
            String field = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            String msg = String.format("[%s]: %s", field, message);
            sb.append(msg).append(";");
        });
        // 全局异常处理
        throw new ValidationException(sb.toString());
    }

    public static <T> boolean validate(T obj){
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        return constraintViolations.isEmpty();
    }
}

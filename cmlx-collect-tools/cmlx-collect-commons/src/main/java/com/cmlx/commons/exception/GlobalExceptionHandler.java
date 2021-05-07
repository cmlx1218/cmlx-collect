package com.cmlx.commons.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 15:04
 * @Desc -> 全局异常处理类 -> 此处主要处理org.springframework.validation.BindException 异常，入参校验
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    public ExceptionResult handBindException(BindException exception) throws Exception {
        String defaultMessage = exception.getAllErrors().get(0).getDefaultMessage();
        //throw EXPF.exception(ErrorCode.Parameter.getCode(), defaultMessage, false);
        return ExceptionResult.error(defaultMessage);
    }

}


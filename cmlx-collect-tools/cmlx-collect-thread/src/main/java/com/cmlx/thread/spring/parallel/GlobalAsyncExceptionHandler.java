package com.cmlx.thread.spring.parallel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 19:55
 * @Desc ->
 **/
@Slf4j
@Component
public class GlobalAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.info("[handleUncaughtException][method({}) params({}) 发生异常]",
                method, params, ex);
    }

}

package com.cmlx.annotation.method.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @Author CMLX
 * @Date -> 2021/5/20 18:36
 * @Desc ->
 **/
@Aspect
@Component
public class CustomCacheAspect {
    /**
     * 在方法执行之前对注解进行处理
     *
     * @param pjd
     * @param customCache 注解
     * @return 返回中的值
     * */
    @Around("@annotation(com.cmlx.annotation.method.cache.CustomCache) && @annotation(customCache)")
    public Object dealProcess(ProceedingJoinPoint pjd, CustomCache customCache) {
        Object result = null;

        if (customCache.key() == null) {
            //TODO throw error
        }

        //TODO 业务场景会比这个复杂的多，会涉及参数的解析如key可能是#{id}这些，数据查询
        //TODO 这里做简单演示，如果key为testKey则返回hello world
        if ("testKey".equals(customCache.key())) {
            return "hello word";
        }

        //执行目标方法
        try {
            result = pjd.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return result;
    }
}


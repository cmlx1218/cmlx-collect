package com.cmlx.redis.cache.lock.annotation;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:15
 * @Desc -> 参数锁，基本类型参数直接使用此参数
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedObject {
    String value() default "";
}

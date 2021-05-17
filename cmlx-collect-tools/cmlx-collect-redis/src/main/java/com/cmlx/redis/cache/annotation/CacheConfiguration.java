package com.cmlx.redis.cache.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:00
 * @Desc ->
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheConfiguration {
    @AliasFor(attribute = "nameSpace")
    String value() default "default";

    @AliasFor(attribute = "value")
    String nameSpace() default "default";

    long expTime() default 0L;
}

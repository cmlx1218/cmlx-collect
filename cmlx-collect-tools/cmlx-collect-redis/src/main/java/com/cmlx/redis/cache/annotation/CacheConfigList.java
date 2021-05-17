package com.cmlx.redis.cache.annotation;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:00
 * @Desc ->
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheConfigList {
    CacheConfiguration[] value();
}


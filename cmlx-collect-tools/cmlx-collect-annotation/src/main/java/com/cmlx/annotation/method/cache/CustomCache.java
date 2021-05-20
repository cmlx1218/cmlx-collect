package com.cmlx.annotation.method.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author CMLX
 * @Date -> 2021/5/20 18:35
 * @Desc ->
 **/
@Target({ ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomCache {

    /**
     * 缓存的key值
     * */
    String key();

}

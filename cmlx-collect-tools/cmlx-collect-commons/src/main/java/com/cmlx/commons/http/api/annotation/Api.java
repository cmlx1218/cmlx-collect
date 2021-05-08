package com.cmlx.commons.http.api.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:06
 * @Desc ->
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Api {
    @AliasFor(attribute = "name")
    String value() default "default";

    @AliasFor(attribute = "value")
    String name() default "default";
}

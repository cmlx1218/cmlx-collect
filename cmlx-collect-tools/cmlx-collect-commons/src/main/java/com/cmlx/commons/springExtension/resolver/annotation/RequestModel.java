package com.cmlx.commons.springExtension.resolver.annotation;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 16:16
 * @Desc ->
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestModel {
    String value();
    boolean require() default true;
}


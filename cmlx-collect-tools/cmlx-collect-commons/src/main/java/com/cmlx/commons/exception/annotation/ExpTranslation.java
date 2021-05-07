package com.cmlx.commons.exception.annotation;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:43
 * @Desc -> 转换器注解
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExpTranslation {
}

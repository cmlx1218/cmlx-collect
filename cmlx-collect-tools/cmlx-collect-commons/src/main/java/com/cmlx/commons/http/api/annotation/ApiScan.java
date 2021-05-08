package com.cmlx.commons.http.api.annotation;

import com.cmlx.commons.http.api.config.ApiRegister;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:07
 * @Desc ->
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ApiRegister.class)
public @interface ApiScan {
    @AliasFor(attribute = "scanPackage")
    String value() default "";
    @AliasFor(attribute = "value")
    String scanPackage() default "";
}

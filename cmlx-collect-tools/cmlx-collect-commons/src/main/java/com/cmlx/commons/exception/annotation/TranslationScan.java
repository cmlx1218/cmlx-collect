package com.cmlx.commons.exception.annotation;

import com.cmlx.commons.exception.TranslationRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:45
 * @Desc ->
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TranslationRegister.class)
public @interface TranslationScan {
    String value() default "";
}

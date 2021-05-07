package com.cmlx.commons.version;

import java.lang.annotation.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:31
 * @Desc -> APP版本控制
 **/
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClientVersion {
    String value() default "1.0.0";
    String android() default "";
    String ios()  default "";
}

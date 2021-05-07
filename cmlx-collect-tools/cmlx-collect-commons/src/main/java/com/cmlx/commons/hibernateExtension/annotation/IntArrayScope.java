package com.cmlx.commons.hibernateExtension.annotation;

import com.cmlx.commons.hibernateExtension.validator.IntArrayScopeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:29
 * @Desc -> 检查传入值是否在数组范围内
 **/
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IntArrayScopeValidator.class)
public @interface IntArrayScope {
    Class<?> clazz();
    String fieldName ();
    String message() default "Invalid parameters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        IntArrayScope[] value();
    }
}


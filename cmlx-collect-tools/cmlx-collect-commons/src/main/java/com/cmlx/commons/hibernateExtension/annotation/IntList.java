package com.cmlx.commons.hibernateExtension.annotation;

import com.cmlx.commons.hibernateExtension.validator.IntListValidator;

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
 * @Date -> 2021/5/7 17:32
 * @Desc ->
 **/
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IntListValidator.class)
public @interface IntList {

    char separated() default ',';

    String message() default "{com.cmlx.common.validator.IntList.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        IntList[] value();
    }
}

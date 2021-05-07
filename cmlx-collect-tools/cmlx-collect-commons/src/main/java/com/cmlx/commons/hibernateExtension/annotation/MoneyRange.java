package com.cmlx.commons.hibernateExtension.annotation;

import com.cmlx.commons.hibernateExtension.validator.MoneyRangeValidator;

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
 * @Date -> 2021/5/7 17:34
 * @Desc -> 金额检查注解
 **/
@Documented
@Constraint(validatedBy = MoneyRangeValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface MoneyRange {
    double min() default 0.01D;

    double max() default Double.MAX_VALUE;

    String message() default "{com.cmlx.commons.validator.MoneyRange.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @Range} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        MoneyRange[] value();
    }
}


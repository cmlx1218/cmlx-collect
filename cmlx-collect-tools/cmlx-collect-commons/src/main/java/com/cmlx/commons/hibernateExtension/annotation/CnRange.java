package com.cmlx.commons.hibernateExtension.annotation;

import com.cmlx.commons.hibernateExtension.validator.CnRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 16:48
 * @Desc ->
 **/

@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CnRangeValidator.class)
public @interface CnRange {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    boolean isConfine() default true;

    boolean ignoreChinese() default false;

    String message() default "{org.hibernate.validator.constraints.Length.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @Range} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CnRange[] value();
    }
}

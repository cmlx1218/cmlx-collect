package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.StrByteLength;
import com.cmlx.commons.support.StringUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:45
 * @Desc ->
 **/
public class StrBytelengthValidator implements ConstraintValidator<StrByteLength, String> {
    private int min;
    private int max;

    @Override
    public void initialize(StrByteLength constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtility.hasText(value)) return true;
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        return length <= max && length >= min;
    }
}

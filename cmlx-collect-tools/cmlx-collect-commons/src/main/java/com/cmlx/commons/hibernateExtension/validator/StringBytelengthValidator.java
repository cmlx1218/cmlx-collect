package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.StringByteLength;
import com.cmlx.commons.support.StringUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:47
 * @Desc ->
 **/
public class StringBytelengthValidator implements ConstraintValidator<StringByteLength, String> {
    private int min;
    private int max;

    @Override
    public void initialize(StringByteLength constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtility.hasText(value)) return true;
        int length = StringUtility.getStringByte(value);
        return length <= max && length >= min;
    }
}

package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.IntArray;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:28
 * @Desc ->
 **/
public class IntArrayValidator implements ConstraintValidator<IntArray,String> {

    private char separated;

    @Override
    public void initialize(IntArray constraintAnnotation) {
        separated = constraintAnnotation.separated();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!StringUtility.hasText(value)) return true;
        return ValidatorUtility.isIntArray(value,separated);
    }
}

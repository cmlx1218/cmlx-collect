package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.Digital;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:19
 * @Desc ->
 **/
public class DigitalValidator implements ConstraintValidator<Digital,String> {

    private long min;
    private long max;

    @Override
    public void initialize(Digital constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtility.hasText(value)) {
            return true;
        }

        if (!ValidatorUtility.isNumeric(value)) {
            return false;
        }
        return value.length() >= min ? value.length() <= max : false;
    }
}

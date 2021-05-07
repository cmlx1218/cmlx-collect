package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.IntList;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:32
 * @Desc ->
 **/
public class IntListValidator implements ConstraintValidator<IntList,String> {

    private char separated;

    @Override
    public void initialize(IntList constraintAnnotation) {
        separated = constraintAnnotation.separated();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!StringUtility.hasText(value)) return true;
        return ValidatorUtility.isIntList(value,separated);
    }
}


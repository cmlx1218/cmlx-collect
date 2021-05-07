package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.StringJson;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:49
 * @Desc ->
 **/
public class StringJsonValidator implements ConstraintValidator<StringJson, String> {
    @Override
    public void initialize(StringJson constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(!StringUtility.hasLength(value)) return true;
        return ValidatorUtility.isJsonString(value);
    }
}

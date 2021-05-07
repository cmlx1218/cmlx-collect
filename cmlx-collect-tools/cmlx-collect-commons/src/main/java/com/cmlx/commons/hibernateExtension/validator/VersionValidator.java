package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.Version;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:50
 * @Desc ->
 **/
public class VersionValidator implements ConstraintValidator<Version, String> {

    @Override
    public void initialize(Version constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtility.hasText(value)) {
            return true;
        }
        return ValidatorUtility.checkClientVersion(value);
    }
}

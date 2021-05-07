package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.IdCard;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:27
 * @Desc ->
 **/
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    @Override
    public void initialize(IdCard constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtility.hasText(value)) return true;
        return ValidatorUtility.isIDCard(value).equals("YES");
    }

}


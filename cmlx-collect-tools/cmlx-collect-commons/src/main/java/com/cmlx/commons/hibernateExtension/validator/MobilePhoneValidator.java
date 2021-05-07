package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.MobilePhone;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:33
 * @Desc -> 手机号校验
 **/
public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, String> {

    public void initialize(MobilePhone arg0) {
    }

    public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
        if (!StringUtility.hasText(arg0)) return true;
        return ValidatorUtility.isMobile(arg0);
    }
}

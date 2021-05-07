package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.CnRange;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.commons.support.ValidatorUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 16:49
 * @Desc ->
 **/
public class CnRangeValidator implements ConstraintValidator<CnRange, String> {

    private int min;
    private int max;
    private boolean isConfine;     // >= <=
    private boolean ignoreChinese; // 忽略中文，1中文=1字符

    @Override
    public void initialize(CnRange constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
        isConfine = constraintAnnotation.isConfine();
        ignoreChinese = constraintAnnotation.ignoreChinese();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (!StringUtility.hasText(value)) return true;
        return ValidatorUtility.isLengthScope(value,min,max,isConfine,ignoreChinese);
    }
}

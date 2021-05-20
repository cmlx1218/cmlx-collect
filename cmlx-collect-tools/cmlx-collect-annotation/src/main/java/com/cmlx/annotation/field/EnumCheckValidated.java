package com.cmlx.annotation.field;

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/20 16:47
 * @Desc ->
 **/
public class EnumCheckValidated implements ConstraintValidator<EnumCheck,Integer> {

    int enumLength;

    @Override
    public void initialize(EnumCheck constraintAnnotation) {
        enumLength = constraintAnnotation.clazz().getEnumConstants().length;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (context instanceof HibernateConstraintValidatorContext) {
            HibernateConstraintValidatorContext validatorContext = (HibernateConstraintValidatorContext) context;
            validatorContext.addExpressionVariable("min", 0);
            validatorContext.addExpressionVariable("max", enumLength - 1);
        }

        if (null == value) {
            return true;
        }
        return enumLength > value;
    }
}

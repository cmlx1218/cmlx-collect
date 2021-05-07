package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.EnumValue;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:22
 * @Desc -> 验证枚举字段的取值范围
 **/
public class EnumValueValidator implements ConstraintValidator<EnumValue, Integer> {
    int enumLength;

    public void initialize(EnumValue constraintAnnotation) {
        enumLength = constraintAnnotation.clazz().getEnumConstants().length;
    }

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

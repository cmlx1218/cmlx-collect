package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.MoneyRange;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:34
 * @Desc -> 金额范围检查类
 **/
public class MoneyRangeValidator implements ConstraintValidator<MoneyRange, Double> {

    private Double min;
    private Double max;

    @Override
    public void initialize(MoneyRange constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (context instanceof HibernateConstraintValidatorContext) {
            HibernateConstraintValidatorContext hcontext = (HibernateConstraintValidatorContext) context;
            hcontext.addExpressionVariable("min", min);
            hcontext.addExpressionVariable("max", max);
        }
        return min <= value && max >= value;
    }
}

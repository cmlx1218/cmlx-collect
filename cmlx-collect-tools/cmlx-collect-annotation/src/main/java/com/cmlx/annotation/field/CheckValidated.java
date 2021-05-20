package com.cmlx.annotation.field;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/20 16:34
 * @Desc ->
 **/
public class CheckValidated implements ConstraintValidator<Check, String> {

    /**
     * 合法参数值，注解中获取
     */
    private List<String> paramValues;

    @Override
    public void initialize(Check constraintAnnotation) {
        // 初始化是获取注解上的值
        paramValues = Arrays.asList(constraintAnnotation.paramValues());
    }

    //ConstraintValidatorContext：可以添加额外的错误消息，或者完全禁用默认的错误信息而使用完全自定义的错误信息
    @Override
    public boolean isValid(String o, ConstraintValidatorContext constraintValidatorContext) {
        if (paramValues.contains(o)) {
            return true;
        }
        // 不在指定的参数列表中
        return false;
    }
}

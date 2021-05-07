package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.IntArrayScope;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:30
 * @Desc ->
 **/
public class IntArrayScopeValidator implements ConstraintValidator<IntArrayScope, Integer> {
    private int[] arrays;
    private boolean noSuchField = false;
    @Override
    public void initialize(IntArrayScope intArrayScope) {
        Class clazz = intArrayScope.clazz();

        try {
            Field field = clazz.getField(intArrayScope.fieldName());
            Object object = field.get(clazz);
            if (object.getClass().isArray()) {
                if (object instanceof int[]) {
                    arrays = (int[]) object;
                }
            }
        } catch (NoSuchFieldException e) {
            noSuchField = true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean isValid(Integer i, ConstraintValidatorContext context) {

        if (noSuchField) {
            context.disableDefaultConstraintViolation();//禁用默认的message的值
            context.buildConstraintViolationWithTemplate("未找到对应校验参数").addConstraintViolation();//重新添加错误提示语句
            return false;
        }
        if (arrays == null) {
            return false;
        }

        for (int array : arrays) {
            if (array == i) {
                return true;
            }
        }
        return false;
    }
}


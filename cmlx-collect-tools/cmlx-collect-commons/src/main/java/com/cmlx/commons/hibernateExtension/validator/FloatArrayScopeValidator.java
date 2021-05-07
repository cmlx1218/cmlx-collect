package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.FloatArrayScope;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:26
 * @Desc ->
 **/
public class FloatArrayScopeValidator implements ConstraintValidator<FloatArrayScope, Float> {
    private float[] arrays;
    private boolean noSuchField = false;
    @Override
    public void initialize(FloatArrayScope floatArrayScope) {
        Class clazz = floatArrayScope.clazz();

        try {
            Field field = clazz.getField(floatArrayScope.fieldName());
            Object object = field.get(clazz);
            if (object.getClass().isArray()) {
                if (object instanceof float[]) {
                    arrays = (float[]) object;
                }
            }
        } catch (NoSuchFieldException e) {
            noSuchField = true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean isValid(Float i, ConstraintValidatorContext context) {

        if (noSuchField) {
            context.disableDefaultConstraintViolation();//禁用默认的message的值
            context.buildConstraintViolationWithTemplate("未找到对应校验参数").addConstraintViolation();//重新添加错误提示语句
            return false;
        }
        if (arrays == null) {
            return false;
        }

        for (float array : arrays) {
            if (array == i) {
                return true;
            }
        }
        return false;
    }
}


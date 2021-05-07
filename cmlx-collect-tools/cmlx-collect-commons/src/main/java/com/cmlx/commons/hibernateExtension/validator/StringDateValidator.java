package com.cmlx.commons.hibernateExtension.validator;

import com.cmlx.commons.hibernateExtension.annotation.StringDate;
import com.cmlx.commons.support.StringUtility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:48
 * @Desc ->
 **/
public class StringDateValidator implements ConstraintValidator<StringDate,String> {

    private String format;

    @Override
    public void initialize(StringDate constraintAnnotation) {
        format = constraintAnnotation.format();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtility.isEmpty(value)) return true;
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

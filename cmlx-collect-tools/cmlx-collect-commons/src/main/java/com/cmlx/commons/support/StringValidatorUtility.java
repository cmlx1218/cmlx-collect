package com.cmlx.commons.support;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:50
 * @Desc ->
 **/
@Slf4j
@UtilityClass
public class StringValidatorUtility {

    /** 全字母规则 正整数规则*/
    public final String STR_ENG_PATTERN="^[a-z0-9A-Z]+$";


    public boolean isMobileNo(String phoneNumber){
        boolean flag = false;
        try {
            Pattern compile = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher matcher = compile.matcher(phoneNumber);
            flag = matcher.matches();
        }catch (Exception e){
            log.error("手机号码格式有误",e);
            flag = false;
        }
        return flag;
    }

    public boolean validateStrEnglish(String str,Integer minLength,Integer maxLength){
        if(StringUtils.isEmpty(str)){
            return Boolean.FALSE ;
        }
        boolean matches = str.matches(STR_ENG_PATTERN);
        if (minLength !=null) {
            if (str.length() < minLength) {
                return Boolean.FALSE ;
            }
        }
        if (maxLength != null) {
            if (str.length() > maxLength) {
                return Boolean.FALSE ;
            }
        }
        if (matches){
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }
    }



}


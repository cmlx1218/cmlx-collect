package com.cmlx.commons.cms;

import com.cmlx.commons.constant.Constant;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:30
 * @Desc ->
 **/
public class SqlUtility {
    // 空或者0-等于  1-不等于  2-大于  3-小于   4-大于等于  5-小于等于  6-包含
    public static String convertSymbol(Integer method){
        if(method == null || method < Constant.SqlCommonMethod.Equal.ordinal() || method > Constant.SqlCommonMethod.Instr.ordinal()) method = 0;
        return Constant.SqlCommonMethod.values()[method].getVal();
    }
}

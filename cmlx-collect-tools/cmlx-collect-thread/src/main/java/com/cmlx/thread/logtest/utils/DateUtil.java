package com.cmlx.thread.logtest.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:56
 * @Desc -> 日期工具类
 **/
public class DateUtil {

    /**
     *  查询当前日期前 多少天 的日期
     * @param dateNum
     * @return
     */
    public static String getBeforeTime(int dateNum){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 0-dateNum);
        date = calendar.getTime();
        return sdf.format(date);
    }

}


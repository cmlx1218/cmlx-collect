package com.cmlx.commons.support;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 15:02
 * @Desc ->
 **/
@UtilityClass
public class DateUtility extends StringUtils {

    public static final Long oneDay = 1000 * 60 * 60 * 24L;

    /**
     * 获取 获取1970至今的天数
     *
     * @return
     */
    public static int getCurDay() {
        TimeZone zone = TimeZone.getDefault();    //默认时区
        long s = System.currentTimeMillis() / 1000;
        if (zone.getRawOffset() != 0) {
            s = s + zone.getRawOffset() / 1000;
        }
        s = s / 86400; //86400 = 24 * 60 * 60 (一天时间的秒数)
        return (int) s;
    }


    public static String convertDateTime(Date currentDate, String datetimeFormat) {
        if (currentDate == null || datetimeFormat == null || "".equals(datetimeFormat)) {
            return "";
        } else {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(datetimeFormat);
                return formatter.format(currentDate);
            } catch (Exception e) {
                return "";
            }
        }
    }

    /**
     * 将指定格式的字符串日期时间转化为Date类型的日期时间
     *
     * @param datetimeStr    指定格式的日期日期字符串
     * @param datetimeFormat 为日期、时间指定的格式
     * @return 转换成的Date类型的日期时间
     * @author xieyj
     */
    public static Date parseDateTime(String datetimeStr, String datetimeFormat) {
        Date parsedDate = null;
        if (datetimeStr == null || "".equals(datetimeStr)) {
            return parsedDate;
        }
        if (datetimeFormat == null || "".equals(datetimeFormat)) {
            return parsedDate;
        }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(datetimeFormat);
            parsedDate = formatter.parse(datetimeStr);
        } catch (Exception e) {
        }
        return parsedDate;
    }

    /**
     * 日期 加 年，月，天，时，分，秒 数
     *
     * @param currentDate   当前时间
     * @param AddType       添加类型  年，月，天，时，分，秒
     * @param dateTimeCount 数量
     * @return 加后的日期
     * @author xieyj
     */
    public static Date addDateTime(Date currentDate, int AddType, int dateTimeCount) {
        Date addedDate = currentDate;
        if (currentDate != null && dateTimeCount != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(AddType, dateTimeCount);
            addedDate = calendar.getTime();
        }
        return addedDate;
    }


    public static String getDateByTimeStamp(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /**
     * 获取当前时间
     * 格式：yyyy-MM-dd
     *
     * @return 字符串形式
     * @author xieyj
     */
    public static String getYYYYMMDD(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(newDate);
    }

    public static String getYYYYMMDDHHMM(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(newDate);
    }

    public static String getYYYYMMDDHHMMBeforeDays(String date, Integer days) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date addDateTime = addDateTime(newDate, Calendar.DAY_OF_WEEK, -days);
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(addDateTime);
    }

    /**
     * 时间格式转换
     * 格式：yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String getYYYYMMDDBybar(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(newDate);
    }

    public static String getYYYYMMDDByDot(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date newDate = null;
        try {
            newDate = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        formatter = new SimpleDateFormat("yyyy.MM.dd");
        return formatter.format(newDate);
    }

    /**
     * 获取当前时间
     * 格式：yyyy-MM-dd
     *
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String getCurrentDate_YYYYMMDD() {
        return convertDateTime(new Date(), "yyyy-MM-dd");
    }

    /**
     * 获取当前时间年月
     *
     * @return
     */
    public String getCurrentDate_YYYYMM() {
        return convertDateTime(new Date(), "yyyyMM");
    }

    /**
     * 获取当前日期
     * 格式：yyyy-MM-dd
     *
     * @return 当前日期
     * @author xieyj
     */
    public static Date getCurrentDateOfYYYYMMDD() {
        return parseDateTime(getCurrentDate_YYYYMMDD(), "yyyy-MM-dd");
    }

    /**
     * 获取指定日期
     * 格式：yyyy-MM-dd
     *
     * @return 指定日期
     * @author xieyj
     */
    public static Date getCurrentDateStrOf_YYYYMMDD(String dateStr) {
        return parseDateTime(dateStr, "yyyy-MM-dd");
    }

    /**
     * 获取当前时间
     * 格式：yyyy-MM-dd HH:mm
     *
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String getCurrentDate_YYYYMMDDHHMM() {
        return convertDateTime(new Date(), "yyyy-MM-dd HH:mm");
    }

    /**
     * 获取当前时间
     * 格式：yyyy-MM-dd HH
     *
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String getCurrentDate_YYYYMMDDHH() {
        return convertDateTime(new Date(), "yyyy-MM-dd HH");
    }

    /**
     * 获取当前时间
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String getCurrentDate_YYYYMMDDHHMMSS() {
        return convertDateTime(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前时间
     * 格式：yyyyMMddHHmmss
     *
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String getCurrentDate_YYYYMMDDHHMMSSWithOutSeparator() {
        return convertDateTime(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 获取当前时间
     * 格式：yyyyMMdd
     *
     * @return
     * @author yangran
     */
    public static String getCurrentDate_YYYYMMDDWithOutSeparator() {
        return convertDateTime(new Date(), "yyyyMMdd");
    }

    /**
     * 转换时间
     * 格式：yyyy-MM-dd
     *
     * @param currentDate 等转换日期
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String convertDateTime_YYYYMMDD(Date currentDate) {
        return convertDateTime(currentDate, "yyyy-MM-dd");
    }

    /**
     * 转换时间
     * 格式：yyyy-MM-dd HH:mm
     *
     * @param currentDate 等转换日期
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String convertDateTime_YYYYMMDDHHMM(Date currentDate) {
        return convertDateTime(currentDate, "yyyy-MM-dd HH:mm");
    }

    /**
     * 转换时间
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @param currentDate 等转换日期
     * @return 当前时间的字符串形式
     * @author xieyj
     */
    public static String convertDateTime_YYYYMMDDHHMMSS(Date currentDate) {
        return convertDateTime(currentDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static boolean isThisWeek(Date date) {
        if (date == null) return false;
        Calendar day = Calendar.getInstance();
        day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String dateStr = convertDateTime_YYYYMMDD(day.getTime());
        Date start = parseDateTime(dateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");

        day.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        dateStr = convertDateTime_YYYYMMDD(day.getTime());
        Date end = parseDateTime(dateStr + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        end = addDateTime(end, Calendar.DAY_OF_WEEK, 7);
        if (date.after(start) && date.before(end))
            return true;
        return false;
    }

    public static String getFourWeeksBeforeMonday(String current) {
        Date currentStr = parseDateTime(current + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date fourWeeksBefore = addDateTime(currentStr, Calendar.WEEK_OF_MONTH, -4);
        return convertDateTime_YYYYMMDDHHMMSS(fourWeeksBefore);
    }

    public static String getYesterday(String current) {
        Date currentStr = parseDateTime(current + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        Date yesterday = addDateTime(currentStr, Calendar.DAY_OF_WEEK, -1);
        return convertDateTime_YYYYMMDDHHMMSS(yesterday);
    }

    /**
     * 昨天的时间yyyy-MM-dd格式
     *
     * @return
     */
    public static String getYesterday_YYYYMMDD() {
        LocalDate now = LocalDate.now();
        LocalDate yestorday = now.minusDays(1L);
        return yestorday.toString();
    }

    /**
     * 明天的时间yyyy-MM-dd格式
     *
     * @return
     */
    public static String getTomorrow_YYYYMMDD() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = now.plusDays(1L);
        return tomorrow.toString();
    }

    /**
     * 获取当前周的周日日期
     *
     * @param date
     * @return
     */
    public static String getSundayInWeek_YYYYMMDD(String date) {
        LocalDate localDate = LocalDate.parse(date);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            return date;
        }
        return localDate.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).toString();
    }

    /**
     * 获取两个日期之间的所有周日
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<String> getSundaysBetweenTwoDate(String startDate, String endDate) throws Exception {
        List<String> sundayList = new ArrayList<>();
        String day = DateUtility.getSundayInWeek_YYYYMMDD(startDate);
        while (DateUtility.compareToDate(day, endDate) < 1) {
            sundayList.add(day);
            day = DateUtility.addDays_YYYYMMDD(7L, day);
        }
        return sundayList;
    }

    /**
     * 获取指定日期当月第一天的日期
     *
     * @param date
     * @return
     */
    public String getFirstDayOfMonth_YYYYMMDD(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.withDayOfMonth(1).toString();
    }

    /**
     * 获取指定日期当月最后一天的日期
     *
     * @param date
     * @return
     */
    public String getLastDayOfMonth_YYYYMMDD(String date) {
        LocalDate localDate = LocalDate.parse(date);
        //return localDate.withDayOfMonth(localDate.getMonth().length(localDate.isLeapYear())).toString();
        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return lastDayOfMonth.toString();


    }

    /**
     * 获取指定日期之间的每月第一天
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static List<String> getFirstDayOfMonthBetweenTwoDate(String startDate, String endDate) throws Exception {
        List<String> days = new ArrayList<>();
        String day = DateUtility.getFirstDayOfMonth_YYYYMMDD(startDate);
        while (DateUtility.compareToDate(day, endDate) < 1) {
            if (DateUtility.compareToDate(day, startDate) >= 0) {
                days.add(day);
            }
            day = DateUtility.addMonths_YYYYMMDD(1L, day);
        }
        return days;
    }

    /**
     * 加上指定月之后的日期 YYYY-MM-DD格式
     *
     * @param addMonths
     * @param date
     * @return
     */
    public static String addMonths_YYYYMMDD(Long addMonths, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate targetDate = localDate.plusMonths(addMonths);
        return targetDate.toString();
    }

    public static Long convertYYYYMMDDHHMMSS_TimeStamp(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(str);
        long timeStamp = date.getTime();
        return timeStamp;
    }

    public static Long convertYYYYMMDDHHMM_TimeStamp(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(str);
        long timeStamp = date.getTime();
        return timeStamp;
    }

    public static Long convertYYYYMMDD_TimeStamp(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(str);
        long timeStamp = date.getTime();
        return timeStamp;
    }


    /**
     * 加上指定天数后的日期 YYYY-MM-DD格式
     *
     * @param addDays
     * @param date
     * @return
     */
    public static String addDays_YYYYMMDD(Long addDays, String date) {
        LocalDate localDate = LocalDate.parse(date);
        LocalDate targetDate = localDate.plusDays(addDays);
        return targetDate.toString();
    }

    public static String getDaysBefore(String current, Integer days) {
        Date currentStr = parseDateTime(current, "yyyy-MM-dd HH:mm:ss");
        Date dayBefore = addDateTime(currentStr, Calendar.DAY_OF_WEEK, days);
        return convertDateTime_YYYYMMDDHHMMSS(dayBefore);
    }

    public static String getWeeksBefore(String current, Integer weeks) {
        Date currentStr = parseDateTime(current, "yyyy-MM-dd HH:mm:ss");
        Date dayBefore = addDateTime(currentStr, Calendar.WEEK_OF_MONTH, weeks);
        return convertDateTime_YYYYMMDDHHMMSS(dayBefore);
    }

    public static Integer getLastWeek(Date current) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Date currentStr = parseDateTime("1585728708155", "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(current);
        Integer thisWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.add(Calendar.DATE, -7);
        Integer lastWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        System.out.println(thisWeek);
        System.out.println(lastWeek);
        return lastWeek;
    }

    public static Integer getLastWeekOfYear(Date current) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(current);
        calendar.add(Calendar.DATE, -7);

        return calendar.get(Calendar.YEAR);
    }

    public static Integer getCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static Long getCurrentWeekStartTime() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        Long time = getDayStartTime(cal.getTime());
        return time;
    }

    public static Long getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public Long getCurrentMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }

    /**
     * 离现在几天前的日期的开始时间
     *
     * @param days
     * @return
     */
    public static Long getDayStartTimeBeforeDays(Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        return getDayStartTime(cal.getTime());
    }


    public static String getYearsBefore(String current, Integer years) {
        Date currentStr = parseDateTime(current, "yyyy-MM-dd HH:mm:ss");
        Date dayBefore = addDateTime(currentStr, Calendar.YEAR, years);
        return convertDateTime_YYYYMMDDHHMMSS(dayBefore);
    }

    public static String getUnixTimestamp() {
        StringBuilder builder = new StringBuilder();
        builder.append(System.currentTimeMillis());
        return builder.substring(0, 10);
    }

    public static String getTimestamp() {
        return System.currentTimeMillis() + "";
    }

    public static Long timestamp() {
        return System.currentTimeMillis();
    }

    public Long currentSecondTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 计算两天之间的天数
     *
     * @param startStr
     * @param endStr
     * @return
     */
    public static int daysBetween(String startStr, String endStr) {
        int daysBetween = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            Date date1 = sdf.parse(startStr);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(date1);

            Date date2 = sdf.parse(endStr);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(date2);

            Calendar date = (Calendar) startDate.clone();

            while (date.before(endDate)) {
                date.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }

        } catch (ParseException e) {
            return -1;
        }
        return daysBetween;
    }

    /**
     * 计算两天之间的年数
     *
     * @param startStr
     * @param endStr
     * @return
     */
    public static int yearsBetween(String startStr, String endStr) {
        int yearsBetween = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = sdf.parse(startStr);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(date1);

            Date date2 = sdf.parse(endStr);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(date2);

            Calendar date = (Calendar) startDate.clone();

            while (date.before(endDate)) {
                date.add(Calendar.YEAR, 1);
                yearsBetween++;
            }

        } catch (ParseException e) {
            return -1;
        }
        return yearsBetween;
    }

    /**
     * 比较日期格式大小
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static int compareToDate(String startTime, String endTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startTime);
        Date end = sdf.parse(endTime);
        if (start.getTime() > end.getTime()) {
            return 1;
        } else if (start.getTime() < end.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 时间戳比较，返回当前与比较时间差值（天数）
     *
     * @param time
     * @return
     */
    public static int compareToNow(Long time) {
        Long val = 0L;
        Long t = new Date().getTime() - time;
        if (t > 0) {
            val = t / (1000 * 3600 * 24);
        }
        return val.intValue();
    }

    /**
     * 获取当天剩余的时间（秒为单位）
     *
     * @return
     */
    public static Long getRestSecondsOfDay() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 23, 59, 59);
        ZoneOffset zoneOffset = ZoneOffset.of("+08:00");
        return end.toEpochSecond(zoneOffset) - start.toEpochSecond(zoneOffset);
    }

    public String timeStamp2Date(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timeStamp));
    }

    public String timeStamp2Date_YYYYMMDDTHHMMSSTIMEZONE(Long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        return sdf.format(new Date(timeStamp));
    }


    /**
     * 下个月第一天日期
     *
     * @return
     */
    public String nextMonthFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar3 = Calendar.getInstance();
        int maxCurrentMonthDay = calendar3.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar3.add(Calendar.DAY_OF_MONTH, maxCurrentMonthDay);
        calendar3.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendar3.getTime());
    }

    /**
     * 获取当月第一天日期
     *
     * @return
     */
    public String currentMonthFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendar3.getTime());
    }

    public String currentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(calendar3.getTime());
    }

    public String lastMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.DAY_OF_MONTH, 0);
        return sdf.format(calendar3.getTime());
    }

    public int getMonthDays(int year, int month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        return localDate.getMonth().length(localDate.isLeapYear());
    }

    /**
     * 获取几天后0点时间戳
     *
     * @param time
     * @param days
     * @return
     */
    public Long getDaysZeroTime(Long time, Integer days) {
        return (time / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset())
                + 24 * 60 * 60 * 1000 * days;
    }


    public String getMonthStartTimeByDateStr(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = sdf.parse(str);
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        return sdformat.format(cale.getTime());
    }

    public String getMonthEndTimeByDateStr(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = sdf.parse(str);
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        cale.set(Calendar.HOUR_OF_DAY, 23);
        cale.set(Calendar.MINUTE, 59);
        return sdformat.format(cale.getTime());
    }

    public String getYYYYMMDDByDateStr(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(str);
        return sdf.format(date);
    }

    /**
     * 获取本星期的第一天
     *
     * @throws ParseException
     */
    public String getFirstDayOfThisWeek() {
        String firstDay;
        Calendar instance = Calendar.getInstance();
        int i = instance.get(Calendar.DAY_OF_WEEK);
        Long firstDayTimesteamp = timestamp() - (i == 1 ? 6 : i - 2) * 60 * 60 * 24 * 1000;
        Date date = new Date(firstDayTimesteamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        firstDay = simpleDateFormat.format(date);
        return firstDay + " 00:00:00";
    }

    /**
     * 获取本星期的最后一天
     *
     * @throws ParseException
     */
    public String getLastDayOfThisWeek() {
        String lastDay;
        Calendar curStartCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = (Calendar) curStartCal.clone();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, 6);
        lastDay = df.format(cal.getTime());
        return lastDay + " 23:59:59";
    }

    public static List<String> getDatesBetweenStartAndEndDate(String startDate, String endDate) throws Exception {
        List<String> days = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);

        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(end);
        tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
        while (tempStart.before(tempEnd)) {
            days.add(dateFormat.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }

        return days;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getCurrentDate_YYYYMM());

        System.out.println(getCurrentMonthStartTime());
        System.out.println(nextMonthFirstDay());
        System.out.println(currentMonthFirstDay());

        System.out.println(currentMonth());
        System.out.println(lastMonth());

        List<String> list = getDatesBetweenStartAndEndDate("2020-11-21", "2020-11-21");
        System.out.println(list);
    }


}


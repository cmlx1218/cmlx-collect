package com.cmlx.commons.support;

import com.cmlx.commons.exception.EXPF;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:10
 * @Desc -> 检查工具类
 **/
@Slf4j
@UtilityClass
public class ValidatorUtility {


    public final int START_YEAR = 1900;
    public final int END_YEAR = 2100;

    private final int[] DAYS_OF_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final String ZERO_STRING = "0";
    private final HashSet<String> AREACODE = new HashSet<>();

    {
        AREACODE.add("091");
        AREACODE.add("082");
        AREACODE.add("081");
        AREACODE.add("071");
        AREACODE.add("065");
        AREACODE.add("064");
        AREACODE.add("063");
        AREACODE.add("062");
        AREACODE.add("061");
        AREACODE.add("054");
        AREACODE.add("053");
        AREACODE.add("052");
        AREACODE.add("051");
        AREACODE.add("050");
        AREACODE.add("046");
        AREACODE.add("045");
        AREACODE.add("044");
        AREACODE.add("043");
        AREACODE.add("042");
        AREACODE.add("041");
        AREACODE.add("037");
        AREACODE.add("036");
        AREACODE.add("035");
        AREACODE.add("034");
        AREACODE.add("033");
        AREACODE.add("032");
        AREACODE.add("031");
        AREACODE.add("023");
        AREACODE.add("022");
        AREACODE.add("021");
        AREACODE.add("015");
        AREACODE.add("014");
        AREACODE.add("013");
        AREACODE.add("012");
        AREACODE.add("011");
    }

    /**
     * 检查是否中英文
     *
     * @param str
     * @return
     */
    public boolean isEnglishOrChinese(String str) {
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次
        if (null == str)
            return false;
        Pattern p = Pattern.compile("^[A-Za-z]*|[\u4E00-\u9FA5]*$");
        Matcher match = p.matcher(str);
        return match.matches();
    }

    /**
     * 检查是否中英文
     *
     * @param str
     * @return
     */
    public boolean isName(String str) {
        // 1、[A-Za-z]* 英文字母的匹配 一次或者多次
        // 2、[\u4E00-\u9FA5]* 汉字匹配 一次或者多次
        if (null == str)
            return false;
        Pattern p = Pattern.compile("^([A-Za-z]+[\\/][A-Za-z]+)|[\u4E00-\u9FA5]*");
        Matcher match = p.matcher(str);
        return match.matches();
    }

    /**
     * 验证字串的长度范围,min为空max不为空,验证是否小于max;min不为空max为空,验证是否大于min,如果忽略中文
     * ,1个中文将按1长度计算,如果不忽略中文,1个中文将按2长度计算
     *
     * @param str
     * @param minLenth
     * @param maxLength
     * @param isConfine
     * @param ignoreChinese
     * @return
     */
    public boolean isLengthScope(String str, Integer minLenth, Integer maxLength, boolean isConfine, boolean ignoreChinese) {
        if (null == str)
            return false;
        int strLenght = str.length();
        if (!ignoreChinese) {
            strLenght = getStringLenght(str);
        }
        if (null == str || "".equals(str) || (minLenth == null && maxLength == null)) {
            return false;
        }
        if (minLenth != null && maxLength == null) {// 验证是否大于min长度
            if (isConfine && strLenght >= minLenth) {// 大于等于min
                return true;
            } else if (!isConfine && strLenght > minLenth) {// 大于min
                return true;
            }
        } else if (minLenth == null && maxLength != null) {
            if (isConfine && strLenght <= maxLength) {// 小于等于max
                return true;
            } else if (!isConfine && strLenght < maxLength) {// 小于max
                return true;
            }
        } else {
            if (isConfine && (minLenth <= strLenght && strLenght <= maxLength)) {
                return true;
            } else if (!isConfine && (minLenth < strLenght && strLenght < maxLength)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证身份证
     *
     * @param IDStr
     * @return
     */
    public String isIDCard(String IDStr) {
        if (null == IDStr)
            return "Null";
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                return errorInfo;
            }
        } catch (NumberFormatException e) {
            log.error(EXPF.getExceptionMsg(e));
        } catch (java.text.ParseException e) {
            log.error(EXPF.getExceptionMsg(e));
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 地区码是否有效 ================
        if (!AREACODE.contains(Ai.substring(0, 2))) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "YES";
        }
        return "YES";
    }

//    /**
//     * 地区编码
//     *
//     * @return
//     */
//    private  Hashtable<String,String> getAreaCode() {
//        Hashtable<String,String> hashtable = new Hashtable<String,String>();
//        hashtable.put("011","北京");
//        hashtable.put("012","天津");
//        hashtable.put("013","河北");
//        hashtable.put("014","山西");
//        hashtable.put("015","内蒙古");
//        hashtable.put("021","辽宁");
//        hashtable.put("022","吉林");
//        hashtable.put("023","黑龙江");
//        hashtable.put("031","上海");
//        hashtable.put("032","江苏");
//        hashtable.put("033","浙江");
//        hashtable.put("034","安徽");
//        hashtable.put("035","福建");
//        hashtable.put("036","江西");
//        hashtable.put("037","山东");
//        hashtable.put("041","河南");
//        hashtable.put("042","湖北");
//        hashtable.put("043","湖南");
//        hashtable.put("044","广东");
//        hashtable.put("045","广西");
//        hashtable.put("046","海南");
//        hashtable.put("050","重庆");
//        hashtable.put("051","四川");
//        hashtable.put("052","贵州");
//        hashtable.put("053","云南");
//        hashtable.put("054","西藏");
//        hashtable.put("061","陕西");
//        hashtable.put("062","甘肃");
//        hashtable.put("063","青海");
//        hashtable.put("064","宁夏");
//        hashtable.put("065","新疆");
//        hashtable.put("071","台湾");
//        hashtable.put("081","香港");
//        hashtable.put("082","澳门");
//        hashtable.put("091","国外");
//        return hashtable;
//    }

    /**
     * @param str
     * @return
     * @Title: isNumeric
     * @Description: 是否全数字
     */
    public boolean isNumeric(String str) {
        if (null == str)
            return false;
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public boolean isPositive(String str) {
        if (null == str) return false;
        Pattern pattern = Pattern.compile("[-]?[0-9]+(\\.[0-9]+)?");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * @param strDate
     * @return
     * @Title: isDate
     * @Description: 验证日期格式
     */
    public boolean isDate(String strDate) {
        if (null == strDate)
            return false;
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))"
                + "[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])"
                + "|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])"
                + "|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|"
                + "(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|"
                + "(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        return m.matches();
    }

    /**
     * @param str
     * @return
     * @Title: isInteger
     * @Description: 整数验证
     */
    public boolean isInteger(String str) {
        if (null == str)
            return false;
        Pattern p = Pattern.compile("^-?\\d*$");
        Matcher match = p.matcher(str);
        return match.matches();
    }

    /**
     * 验证正整数
     *
     * @param str
     * @return
     */
    public boolean isPositiveInteger(String str) {
        if (null == str)
            return false;
        Pattern p = Pattern.compile("^[1-9]+\\d*$");
        Matcher match = p.matcher(str);
        return match.matches() || ZERO_STRING.equals(str);
    }

    /**
     * 全英文字母
     *
     * @param str
     * @return
     */
    public boolean isEnglish(String str) {
        if (null == str)
            return false;
        Pattern p = Pattern.compile("^[A-Za-z]*$");
        Matcher match = p.matcher(str);
        return match.matches();
    }

    /**
     * 验证是否中英文混合
     *
     * @param str
     * @return
     */
    public boolean isNonSpecialChar(String str) {
        if (null == str)
            return false;
        Pattern p = Pattern.compile("^[A-Za-z\u4E00-\u9FA5\\d]*$");
        Matcher match = p.matcher(str);
        return match.matches();
    }

    /**
     * 验证过期时间
     *
     * @param startHour
     * @param endHour
     * @return
     */
    public boolean isHourZone(String startHour, String endHour, int hour) {
        boolean flag = false;
        if (startHour != null && endHour != null) {
            if (isHour(startHour) && isHour(endHour)) {
                int sHour = Integer.parseInt(startHour);
                int eHour = Integer.parseInt(endHour);
                flag = (eHour - sHour >= hour);
            }
        }
        return flag;

    }

    /**
     * 结束时间大于开始时间
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean isGTimeZone(String startDate, String endDate) {
        boolean flag = false;
        if (startDate != null && endDate != null) {
            if (isValidDate(startDate) && isValidDate(endDate)) {
                flag = (Integer.parseInt(endDate) > Integer.parseInt(startDate));
            }
        }
        return flag;
    }

    /**
     * 结束时间等于开始时间
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public boolean isETimeZone(String startDate, String endDate) {
        boolean flag = false;
        if (startDate != null && endDate != null) {
            if (isValidDate(startDate) && isValidDate(endDate)) {
                flag = (Integer.parseInt(endDate) > Integer.parseInt(startDate) || Integer.parseInt(endDate) == Integer.parseInt(startDate));
            }
        }
        return flag;
    }

    /**
     * 验证座机
     *
     * @param telephoeNo
     * @return
     */
    public boolean isTelephoeNo(String telephoeNo) {
        // 1、\\d{3,4} 区号 3位或者4位的匹配
        // 2、\\d{7,8} 号码 7味或者8位的匹配
        // 3、(\\d{3,4})? 分机号3位或者4位的匹配 ？可匹配一次或者两次
        if (isNull(telephoeNo))
            return false;
        Pattern p = Pattern.compile("^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$");
        Matcher match = p.matcher(telephoeNo);
        return match.matches();
    }

    /**
     * 验证手机号码,多个使用,分隔
     *
     * @param mobileNo
     * @return
     */
    public boolean isMobile(String mobileNo) {
        if (isNull(mobileNo))
            return false;
        Pattern p = Pattern.compile("^(((\\+86)|(86))?((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(19[0-9])|(17[0-9])|(18[0-9]))\\d{8}\\,)*(((\\+86)|(86))?((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(19[0-9])|(17[0-9])|(18[0-9]))\\d{8})$");
        Matcher match = p.matcher(mobileNo);
        return match.matches();
    }

    /**
     * 验证邮箱格式
     *
     * @param email
     * @return
     */
    public boolean isEmail(String email) {
        // 1、\\w+表示@之前至少要输入一个匹配字母或数字或下划线 \\w 单词字符：[a-zA-Z_0-9]
        // 2、(\\w+\\.)表示域名. 如新浪邮箱域名是sina.com.cn
        // {1,3}表示可以出现一次或两次或者三次.
        if (null == email)
            return false;
        String reg = "\\w+@(\\w+\\.){1,3}\\w+";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 验证小时是否正确
     *
     * @param hour
     * @return
     */
    public boolean isHour(String hour) {
        if (null == hour)
            return false;
        boolean flag = false;
        Pattern pattern = Pattern.compile("^[0-2][0-9]$");
        Matcher matcher = pattern.matcher(hour);
        flag = matcher.matches();
        int firstNum = Integer.parseInt(hour.substring(0, 1));
        if (flag && firstNum == 2) {
            int secondNum = Integer.parseInt(hour.substring(1, 2));
            flag = secondNum < 4; // 时间小于24时
        }
        return flag;
    }

    /**
     * 验证日期
     *
     * @param dayStr
     * @return
     */
    public boolean isDay(String dayStr) {
        if (null == dayStr)
            return false;
        Pattern p = Pattern.compile("^\\d{2}\\d{2}\\d{2}$");
        Matcher match = p.matcher(dayStr);
        // 格式验证通过 yyMMdd
        if (match.matches()) {
            int month = Integer.parseInt(dayStr.substring(2, 4)); // 月
            int day = Integer.parseInt(dayStr.substring(4, 6)); // 日
            if (!isValidMonth(month)) {
                return false; // 月份不合法
            }
            if (!(day >= 1 && day <= DAYS_OF_MONTH[month - 1])) {
                return false; // 日期不合法
            }
            return true;
        }
        return false;
    }

    public boolean isValidDate(String date) {
        if (null == date)
            return false;
        // 1、 \\d{4} 年，\\d{2}月，\\d{2}日匹配
        Pattern p = Pattern.compile("^\\d{4}\\d{2}\\d{2}$");
        Matcher match = p.matcher(date);
        // 格式验证通过 yyyyMMdd
        if (match.matches()) {
            int year = Integer.parseInt(date.substring(0, 4)); // 年
            int month = Integer.parseInt(date.substring(4, 6)); // 月
            int day = Integer.parseInt(date.substring(6, 8)); // 日
            if (!isValidYear((year))) {
                return false; // 年份不在有效年份中
            }
            if (!isValidMonth(month)) {
                return false; // 月份不合法
            }
            if (!isValidDay(year, month, day)) {
                return false; // 日期不合法
            }
            return true;
        }
        return false;
    }

    /**
     * 验证有效年份 1900-2100
     *
     * @param year
     * @return
     */
    public boolean isValidYear(int year) {
        return year >= START_YEAR && year <= END_YEAR;
    }

    /**
     * 验证月份是否在有效月份内
     *
     * @param month
     * @return
     */
    public boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }

    /**
     * 检查天数是否在有效的范围内，因为天数会根据年份和月份的不同而不同 所以必须依赖年份和月份进行检查
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public boolean isValidDay(int year, int month, int day) {
        if (month == 2 && isLeapYear(year))// 闰年且是2月份
        {
            return day >= 1 && day <= 29;
        }
        return day >= 1 && day <= DAYS_OF_MONTH[month - 1];// 其他月份
    }

    /**
     * 验证是否是闰年
     *
     * @param year
     * @return
     */
    public boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 验证用户名注册是否合法-----只含有汉字、数字、字母、下划线不能以下划线开头和结尾
     *
     * @param userName
     * @return
     */
    public boolean isRegUserName(String userName) {
        if (null == userName)
            return false;
        String str = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4E00-\u9FA5]+$";
        Pattern p = Pattern.compile(str);
        Matcher match = p.matcher(userName);
        return match.matches();
    }

    /**
     * 验证是否经纬度坐标
     *
     * @param lng
     * @param lat
     * @return
     */
    public boolean isLngLat(double lng, double lat) {
        return (lng >= -180.0D) && (lng <= 180.0D) && (lat >= -90.0D) && (lat <= 90.0D);
    }

    /**
     * 验证是否为空
     *
     * @param str
     * @return
     */
    public boolean isNull(String str) {
        str = StringUtility.replaceSpace(str);
        return null == str || "".equals(str);
    }

    /**
     * 返回字串长度,如果包含汉字，把汉字当做2byte计算
     *
     * @param str
     * @return
     */
    public int getStringLenght(String str) {
        int stringLenght = str.length();
        // 获取中文个数
        int chineseCount = getChineseCharCount(str);
        int otherCount = str.length() - chineseCount;
        if (stringLenght != otherCount) {
            stringLenght = otherCount + chineseCount * 2;
        }
        return stringLenght;
    }

    /**
     * 返回汉字个数
     *
     * @param str
     * @return
     */
    private int getChineseCharCount(String str) {
        int count = 0;
        String regEx = "[\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }

    /**
     * 检查 数字串：1,2,3 已修改为接受0及负整数
     *
     * @param str
     * @param separated
     * @return
     */
    public boolean isIntArray(String str, char separated) {
        String regEx = "^(0" + separated + "|-[1-9]+\\d*" + separated + "|[1-9]+\\d*" + separated + ")*(0|-[1-9]+\\d*|[1-9]+\\d*)$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检查字符串 [1,2,3]
     *
     * @param str
     * @param separated
     * @return
     */
    public boolean isIntList(String str, char separated) {
        String regEx = "^\\[(0" + separated + "|-[1-9]+\\d*" + separated + "|[1-9]+\\d*" + separated + ")*(0|-[1-9]+\\d*|[1-9]+\\d*)]$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检查字串是否权限格式 例：user:create:*
     *
     * @param str
     * @param separated
     * @return
     */
    public boolean isPermission(String str, char separated) {
        String regEx = "^((\\*{1}|\\w+)" + separated + ")*(\\*{1}|\\w+)$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 检查是否包含在父级路径中
     *
     * @param superiorPath
     * @param obj
     * @return
     */
    public boolean superiorPathContain(String superiorPath, Object obj) {
        return contain(superiorPath, obj, '/');
    }

    /**
     * 包含
     * 例：1,2,3,4,5 包含2
     *
     * @return
     */
    public boolean contain(String checkStr, Object obj, char separated) {
        StringBuilder patternStr = new StringBuilder("(?<=^|");
        patternStr.append(separated).append(")").append(obj).append("(?=").append(separated).append("|$)");
        Pattern pattern = Pattern.compile(patternStr.toString());
        Matcher matcher = pattern.matcher(checkStr);
        return matcher.find();
    }

    /**
     * 检查APP版本格式 例：1.0.0
     * 标准版本格式
     * alpha - 内部版本
     * Beta  - 测试版本
     * RC    - 即将发布
     * Release - 发布版
     * 1   .   1    .    1    .   170707_base
     * 主版本  子版本   阶段版本    日期版本   字母版本
     *
     * @param clientVersion
     * @return
     */
    public boolean checkClientVersion(String clientVersion) {
        if (!StringUtility.hasText(clientVersion)) return false;
        Pattern pattern = Pattern.compile("^[0-9][0-9]*.([0-9]|[1-9][0-9]*).([0-9]|[1-9][0-9]*)$");
        return pattern.matcher(clientVersion).find();
    }

    /**
     * 对比版本号
     *
     * @param clientVersion app版本号
     * @param apiVersion    服务端指定版本号
     * @return 大于0代表服务端指定版本号较高
     */
    public int compare(String clientVersion, String apiVersion) {
        // 检查接口版本格式，格式错误返回需更新信息
        if (!ValidatorUtility.checkClientVersion(clientVersion)) {
            return 1;
        }
        String[] clientVersions = clientVersion.split("\\.");
        String[] apiVersions = apiVersion.split("\\.");
        for (int i = 0; i < apiVersions.length; i++) {
            Integer appv = Integer.valueOf(clientVersions[i]);
            Integer apiv = Integer.valueOf(apiVersions[i]);
            if (apiv == appv) {
                continue;
            } else if (apiv > appv) {
                return 1;
            } else {
                return -1;
            }
        }
        return 0;
    }

    /**
     * 验证json串
     *
     * @param json
     * @return
     */
    public boolean isJsonString(String json) {
        try {
            if (null == json) return false;
            json = json.replaceAll(" ", "");
            if (!StringUtility.hasLength(json)) return false;
            JsonUtility.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

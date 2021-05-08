package com.cmlx.commons.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:33
 * @Desc ->
 **/
public class Constant {

    /**
     * sql公共符号
     */
    public enum SqlCommonMethod {
        Equal("="),
        UnEqual("!="),
        GreaterThan(">"),
        LessThan("<"),
        GreaterThanOrEqual(">="),
        LessThanOrEqual("<="),
        Instr("instr");

        private String val;

        SqlCommonMethod(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    public enum DriversOs {
        Android,
        iOS,
        Web
    }

    public enum ShortUrlType {
        LoginTerminal("loginTerminal"),
        ActiveTerminal("activeTerminal"),
        JoinGroup("joinGroup"),
        UserProfile("userProfile"),
        LoginCoach("loginCoach"),
        LoginPC("loginPC");
        private String val;

        ShortUrlType(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    /**
     * 圈子通知类型
     */
    public enum GroupNotificationType {
        Entry, //0-加入
        Quit, //1-退出
        Remove, //2-移除
        MasterTransfer,//3-群主转移
        GroupNameChange,//4-群名改变
        EntryByQRCode, //5-扫码加入

    }

    @Getter
    @AllArgsConstructor
    public enum Sex {
        Man(1, "男"),
        WOMAN(2, "女");

        public Integer code;
        public String msg;

        private static HashMap<Integer, Sex> data = new HashMap<>();

        static {
            for (Sex d : Sex.values()) {
                data.put(d.code, d);
            }
        }

        public static Sex parse(Integer code) {
            if (data.containsKey(code)) {
                return data.get(code);
            }
            return null;
        }
    }



}

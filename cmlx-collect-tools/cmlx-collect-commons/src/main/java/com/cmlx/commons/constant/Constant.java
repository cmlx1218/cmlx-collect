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

    public enum QosEnum {

        /**
         * QoS0：“至多一次”，消息基于TCP/IP网络传输，没有回应，消息可能到达服务器一次，也可能根本不会到达
         * QoS1：“至少一次”，服务器接收到消息会被确认，通过传输一个PUBACK消息。如果有一个可以辨认的传输失败，无论是通讯连接还是发送设备，
         * 还是过了一段时间确认信息没有收到，发送方都会将消息头的DUP位置1，然后再次发送消息。消息最少一次到达服务器。可能会出现重复消息
         * QoS2：“只有一次”，确保消息到达一次，在QoS level 1上附加的协议流保证了重复的消息不会传送到接收的应用
         */
        QoS0(0), QoS1(1), QoS2(2);

        QosEnum(int qos) {
            this.value = qos;
        }

        private final int value;

        public int value() {
            return this.value;
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

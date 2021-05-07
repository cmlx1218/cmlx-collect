package com.cmlx.commons.generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:09
 * @Desc -> 订单ID、优惠券编码生成
 *  *       年后两位+月+日 + 用户ID后4位 + idGenerator
 *  *       6位       +     4位     +     16位
 **/
public class CodeGenerator {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");
    /**
     * 主机地址
     */
    private long hostId;
    /**
     * 毫秒自增值
     */
    private long sequence = 0L;
    /**
     * 起始时间 2017-07-01 12:00:00
     */
    private long twepoch = 1498838400000L;
    /**
     * 主机值占位
     */
    private long hostIdBits = 16L;
    /**
     * 毫秒自增占位,该参数决定1毫秒之内并发数量
     */
    private long sequenceBits = 6L;
    /**
     * 主机值偏移位
     */
    private long hostIdShift = sequenceBits;
    /**
     * 时间戳偏移位
     */
    private long timestampShift = sequenceBits + hostIdBits;
    /**
     * HostId 最大值
     */
    private long maxHostId = -1L ^ (-1L << hostIdBits);
    /**
     * 毫秒自增最大值
     */
    private long maxSequence = -1L ^ (-1L << sequenceBits);
    /**
     * 最后请求时间
     */
    private long lastTimestamp = 0L;

    private Lock lock = new ReentrantLock();

    public CodeGenerator(long hostId) {
        if (hostId > maxHostId || maxHostId < 0)
            throw new IllegalArgumentException(String.format("host Id can't be greater than %d or less than 0", maxHostId));
        this.hostId = hostId;
    }

    public String nextId(Long uid) {
        lock.lock();
        try {
            long timestamp = timeGen();

            if (timestamp < lastTimestamp) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }

            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & maxSequence;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }
            lastTimestamp = timestamp;
            long time = ((timestamp - twepoch) << timestampShift) | (hostId << hostIdShift) | sequence;

            String format = DATE_FORMAT.format(new Date(timestamp));
            StringBuilder builder = new StringBuilder(format.subSequence(0,6));
            String userIdStr = uid + "";
            //update by xieyj on 2017-12-12 for userId length <= 4
            builder.append(userIdStr.length()<=4?userIdStr :userIdStr.subSequence(userIdStr.length()-4,userIdStr.length()));
            builder.append(time);
            return builder.toString();
        } finally {
            lock.unlock();
        }
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
}


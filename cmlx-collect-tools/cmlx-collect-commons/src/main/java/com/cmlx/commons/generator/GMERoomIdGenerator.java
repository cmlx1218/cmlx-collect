package com.cmlx.commons.generator;

import com.cmlx.commons.encrypt.EncryptorUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:10
 * @Desc -> 语音房间生成类
 **/
public class GMERoomIdGenerator {
    private final String jointMark = "_";
    private String defaultEncryptKey;
    @Autowired
    EncryptorUtility encryptorUtility;

    public GMERoomIdGenerator(String defaultEncryptKey) {
        this.defaultEncryptKey = defaultEncryptKey;
    }

    public String doubleRoomId(Long userId1, Long userId2) throws Exception {
        List<Long> userIds = Arrays.asList(userId1, userId2);
        return this.doubleRoomId(userIds);
    }

    public String doubleRoomId(List<Long> userIds) throws Exception {
        StringBuilder builder = new StringBuilder();
        userIds.stream().sorted(Comparator.comparing(e -> e.longValue())).forEach(e -> builder.append(e).append(jointMark));
        builder.append(defaultEncryptKey);
        String makeGMERoomId = encryptorUtility.encryptNoSalt(builder.toString(), EncryptorUtility.OPERATION.MD5);
        return makeGMERoomId;
    }

}

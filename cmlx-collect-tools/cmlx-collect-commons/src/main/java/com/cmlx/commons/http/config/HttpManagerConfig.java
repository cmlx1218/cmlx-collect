package com.cmlx.commons.http.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:08
 * @Desc ->
 **/
@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class HttpManagerConfig {
    /**
     * 响应超时
     */
    private int timeOutSocket;
    /**
     * 建立连接超时
     */
    private int timeOutConnection;
    /**
     * 获取连接超时
     */
    private int timeOutRequestConnection;
    /**
     * 重试次数
     */
    private int retryNumber;
    /**
     * 最大连接数
     */
    private int maxTotal;

    public static Builder custom() {
        return new Builder();
    }

    public static Builder copy(final HttpManagerConfig config) {
        if (null == config) return null;
        return new Builder().timeOutSocket(config.timeOutSocket()).timeOutConnection(config.timeOutConnection())
                .timeOutRequestConnection(config.timeOutRequestConnection()).retryNumber(config.retryNumber()).maxTotal(config.maxTotal());
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder {
        private int timeOutSocket;
        private int timeOutConnection;
        private int timeOutRequestConnection;
        private int retryNumber;
        private int maxTotal;

        Builder() {
            timeOutSocket = 5000;
            timeOutConnection = 2000;
            timeOutRequestConnection = 5000;
            retryNumber = 2;
            maxTotal = 500;
        }

        public HttpManagerConfig build() {
            return new HttpManagerConfig(timeOutSocket, timeOutConnection, timeOutRequestConnection, retryNumber, maxTotal);
        }
    }
}


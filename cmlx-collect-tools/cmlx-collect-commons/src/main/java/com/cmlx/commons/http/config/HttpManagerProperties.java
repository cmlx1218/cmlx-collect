package com.cmlx.commons.http.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:08
 * @Desc ->
 **/
@Data
@ConfigurationProperties(prefix = "appserver.http")
public class HttpManagerProperties {
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

    private Map<String,HttpClientProperties> clientProperties;
}

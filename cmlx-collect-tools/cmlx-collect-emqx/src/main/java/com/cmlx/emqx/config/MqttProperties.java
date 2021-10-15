package com.cmlx.emqx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author CMLX
 * @Date -> 2021/10/15 14:43
 * @Desc -> mqtt配置文件信息
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

    private String brokerUrl;
    private String clientId;
    private String username;
    private String password;


}

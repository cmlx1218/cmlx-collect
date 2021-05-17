package com.cmlx.redis.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 16:59
 * @Desc ->
 **/
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisCacheProperties {
    private String packagesToScan;
    private Long expiredTime = 1800L;
    private Integer pageSize = 20;
}


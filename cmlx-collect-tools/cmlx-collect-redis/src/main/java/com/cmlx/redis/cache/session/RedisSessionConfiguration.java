package com.cmlx.redis.cache.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:18
 * @Desc ->
 **/
@Configuration
@ConditionalOnProperty(prefix = "spring.redis.session", name = "enabled")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
public class RedisSessionConfiguration {
}

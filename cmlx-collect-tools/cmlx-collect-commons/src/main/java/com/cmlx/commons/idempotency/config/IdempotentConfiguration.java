package com.cmlx.commons.idempotency.config;

import com.cmlx.commons.idempotency.core.aop.IdempotentAspect;
import com.cmlx.commons.idempotency.core.keyresolver.IdempotentKeyResolver;
import com.cmlx.commons.idempotency.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.cmlx.commons.idempotency.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.cmlx.commons.idempotency.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author cmlx
 * @desc
 * @date 2022/5/7 10:26
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(IdempotentConfiguration.class)
public class IdempotentConfiguration {


    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }


}

package com.cmlx.test.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author CMLX
 * @Date -> 2021/8/3 17:24
 * @Desc -> 连接池注入配置信息
 **/
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory factory;

    /**
     * @return
     * @description RedisTemplate 对五种数据结构分别定义了操作
     * redisTemplate.opsForValue(); // 操作字符串
     * redisTemplate.opsForHash(); // 操作hash
     * redisTemplate.opsForList(); // 操作list
     * redisTemplate.opsForSet(); // 操作set
     * redisTemplate.opsForZSet(); // 操作有序set
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * StringRedisTemplate 是 RedisTemplate<K, V>  子类
     * StringRedisTemplate 只能对 key=String，value=String 的键值对进行操作，RedisTemplate 可以对任何类型的 key-value 键值对操作。
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


}

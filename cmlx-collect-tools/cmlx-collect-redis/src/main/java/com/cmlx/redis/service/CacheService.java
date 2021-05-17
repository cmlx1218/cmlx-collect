package com.cmlx.redis.service;

import com.alibaba.fastjson.JSON;
import com.cmlx.redis.cache.RedisCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:20
 * @Desc ->
 **/
public abstract class CacheService<S> extends BaseService<S> {
    protected static final String UNDERLINE = "_";
    protected static final String COLON = ":";

    @Autowired
    CacheManager cacheManager;
    @Autowired
    protected StringRedisTemplate redisTemplate;
    @Autowired
    protected RedisCacheProperties redisCacheProperties;
    @Autowired
    protected RedisTemplate template;

    @Override
    protected void post() {
        super.post();
    }

    protected boolean checkKey(String nameSpace, Object key) {
        StringBuilder builder = new StringBuilder(nameSpace);
        builder.append(COLON).append(key);
        return redisTemplate.persist(builder.toString());
    }

    /**
     * 重置key过期时间
     *
     * @param key
     */
    protected void expire(String key, Long expiredTime) {
        redisTemplate.expire(key, expiredTime, TimeUnit.SECONDS);
    }

    /**
     * @param baseSpace
     * @param tailSpace
     * @return
     */
    protected String nameSpace(String baseSpace, Object tailSpace) {
        StringBuilder builder = new StringBuilder(baseSpace);
        builder.append(UNDERLINE).append(tailSpace);
        return builder.toString();
    }

    /**
     * 获取缓存key
     *
     * @param
     * @return
     */
    protected String getCacheKey(String nameSpace, Object key) {
        StringBuilder builder = new StringBuilder(nameSpace);
        builder.append(COLON).append(key);
        return builder.toString();
    }

    /**
     * redis批量获取指定命名空间的指定key的值
     *
     * @param clazz
     * @param nameSpace
     * @param keys
     * @param <T>
     * @return
     */
    protected <T> List<T> executePipelined(Class<T> clazz, String nameSpace, List<Long> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>();
        }
        List<Object> objects = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Object key : keys) {
                connection.get(getRedisCacheKey(nameSpace, key).getBytes());
            }
            return null;
        }, redisTemplate.getValueSerializer());
        List<T> list = new ArrayList<>();
        if (objects != null && !objects.isEmpty()) {
            for (Object o : objects) {
                list.add(JSON.parseObject(o.toString(), clazz));
            }
        }
        return list;
    }

    protected String getRedisCacheKey(String nameSpace, Object key) {
        return nameSpace + COLON + key;
    }

    /**
     * redis 删除一个工作空间下的所以key
     *
     * @param nameSpace
     */
    protected void executePipelinedDelete(String nameSpace) {
        Set<String> keys = redisTemplate.keys(nameSpace + COLON + "*");
        if (CollectionUtils.isEmpty(keys)) return;
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        List<Object> objects = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String str : keys)
                connection.del(keySerializer.serialize(str));
            return null;
        }, redisTemplate.getValueSerializer());
    }

    /**
     * redis删除指定工作空间下的指定key
     *
     * @param nameSpace
     * @param keys
     */
    protected void executePipelinedDelete(String nameSpace, List<Long> keys) {
        if (CollectionUtils.isEmpty(keys)) return;
        RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        List<Object> objects = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Object str : keys)
                connection.del(keySerializer.serialize(getRedisCacheKey(nameSpace, str)));
            return null;
        }, redisTemplate.getValueSerializer());
    }
}


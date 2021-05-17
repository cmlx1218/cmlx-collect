package com.cmlx.redis.cache.lock;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.EXPF;
import com.cmlx.redis.cache.lock.exception.RedisCacheException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Random;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:07
 * @Desc -> Redis锁
 **/
@Component
public class RedisLock implements Serializable {

    private static final long MILLI_NANO_TIME = 2000 * 1000l;
    private static final int LOCKED = 1;
    private static final Random RANDOM = new Random();
    protected static final String COLON = ":";

    private RedisTemplate redisTemplate;

    public RedisLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 加锁
     *
     * @param timeOut 取锁超时时间
     * @param expire  锁自动过期时间
     */
    public Boolean lock(String cacheKey, long timeOut, long expire) throws Exception {
        try {
            Object execute = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                long nanoTime = System.nanoTime();
                long lockTimeOut = timeOut * MILLI_NANO_TIME;
                try {
                    while (System.nanoTime() - nanoTime < lockTimeOut) {
                        if (connection.setNX(cacheKey.getBytes(), redisTemplate.getValueSerializer().serialize(LOCKED))) {
                            connection.expire(cacheKey.getBytes(), expire);
                            return true;
                        }
                        Thread.sleep(3, RANDOM.nextInt(30));
                    }
                    return false;
                } catch (Exception e) {
                    throw new RedisCacheException(e.getMessage(), EXPF.exception(ErrorCode.CacheLockError, true));
                }
            });
            return (Boolean) execute;
        } catch (RedisCacheException e) {
            throw (Exception) e.getCause();
        }
    }

    public void unlock(String cacheKey) {
        redisTemplate.execute(connection -> {
            connection.del(cacheKey.getBytes());
            return null;
        }, true);
    }

    public String madeCacheKey(String prefix, Object key) {
        return prefix + COLON + key;
    }
}


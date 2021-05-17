package com.cmlx.redis.cache.lock.exception;

import org.springframework.dao.DataAccessException;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:16
 * @Desc ->
 **/
public class RedisCacheException extends DataAccessException {
    public RedisCacheException(String msg) {
        super(msg);
    }

    public RedisCacheException(String msg, Throwable cause) {
        super(msg, cause);
    }
}


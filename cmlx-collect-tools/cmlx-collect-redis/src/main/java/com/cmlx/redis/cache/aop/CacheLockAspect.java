package com.cmlx.redis.cache.aop;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.EXPF;
import com.cmlx.commons.support.EntityPropertyUtility;
import com.cmlx.commons.support.StringUtility;
import com.cmlx.redis.cache.lock.RedisLock;
import com.cmlx.redis.cache.lock.annotation.CacheLock;
import com.cmlx.redis.cache.lock.annotation.LockedObject;
import com.cmlx.redis.cache.lock.exception.RedisCacheException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:01
 * @Desc -> 缓存锁切面
 **/
@Slf4j
@Aspect
@Component
@Order(4)  // 执行顺序
public class CacheLockAspect {

    @Autowired
    RedisLock redisLock;

    @Around("@annotation(cacheLock)")
    public Object around(ProceedingJoinPoint pjp, CacheLock cacheLock) throws Throwable {
        String prefix = cacheLock.lockedPrefix();
        String methodKey = cacheLock.value();
        String cacheKey;
        if (StringUtility.hasLength(methodKey)) {
            cacheKey = redisLock.madeCacheKey(prefix, methodKey);
        } else {
            Object[] args = pjp.getArgs();
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Annotation[][] parameterAnnotations = methodSignature.getMethod().getParameterAnnotations();
            Object lockObject = getLockObject(parameterAnnotations, args);
            if(null == lockObject) return pjp.proceed();
            cacheKey = redisLock.madeCacheKey(prefix, lockObject);
        }
        Boolean lock = redisLock.lock(cacheKey, cacheLock.timeOut(), cacheLock.expireTime());
        if(!lock) {
            log.info("{}分布式锁获取失败",cacheKey);
            throw EXPF.exception(ErrorCode.InProgress,true); // 取锁失败，业务处理中
        }
        try {
            log.info("{}分布式锁获取成功",cacheKey);
            // 执行目标方法
            return pjp.proceed();
        }finally {
            // 执行完目标方法之后解除缓存锁
            redisLock.unlock(cacheKey);
        }
    }


    private Object getLockObject(Annotation[][] annotations, Object[] args) {
        if (null == annotations || annotations.length == 0)
            throw new RedisCacheException("Can not find @LockedObject[annotations Empty]");
        if (null == args || args.length == 0) throw new RedisCacheException("Can not find @LockedObject[args Empty]");
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof LockedObject) {
                    LockedObject lockedObject = (LockedObject) annotations[i][j];
                    Object arg = args[i];
                    return getLockObject(lockedObject,arg);
                }
            }
        }
        return null;
    }

    private Object getLockObject(LockedObject lockedObject, Object arg) {
        if (null == arg) return arg;
        Class<?> aClass = arg.getClass();
        if (null != lockedObject && StringUtility.hasLength(lockedObject.value())) {
            try {
                PropertyDescriptor propertyDescriptor;
                if(aClass.getName().equals("java.util.HashMap")){
                    Map dataMap = (Map)arg;
                    return dataMap.get(lockedObject.value());
                }else{
                    propertyDescriptor = EntityPropertyUtility.getPropertyDescriptor(aClass, lockedObject.value());
                }
                Method readMethod = propertyDescriptor.getReadMethod();
                return readMethod.invoke(arg);
            } catch (Exception e) {
                throw new RedisCacheException("Can not find @LockedObject[Object Empty]");
            }
        } else if ((aClass == long.class) || (aClass == Long.class)) {
            return arg;
        } else if ((aClass == int.class) || (aClass == Integer.class)) {
            return arg;
        } else if ((aClass == char.class) || (aClass == Character.class)) {
            return arg;
        } else if ((aClass == short.class) || (aClass == Short.class)) {
            return arg;
        } else if ((aClass == double.class) || (aClass == Double.class)) {
            return arg;
        } else if ((aClass == float.class) || (aClass == Float.class)) {
            return arg;
        } else if ((aClass == boolean.class) || (aClass == Boolean.class)) {
            return arg;
        } else if (aClass == String.class) {
            return arg;
        } else if (aClass == java.sql.Date.class) {
            return arg;
        } else if (aClass == BigDecimal.class) {
            return arg;
        } else {
            return null;
        }
    }
}


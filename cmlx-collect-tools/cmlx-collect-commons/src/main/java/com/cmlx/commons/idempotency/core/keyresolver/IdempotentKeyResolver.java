package com.cmlx.commons.idempotency.core.keyresolver;

import com.cmlx.commons.idempotency.core.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * @author cmlx
 * @desc 幂等 Key 解析器接口
 * @date 2022/5/6 20:06
 */
public interface IdempotentKeyResolver {

    /**
     * 解析一个 Key
     *
     * @param idempotent 幂等注解
     * @param joinPoint  AOP 切面
     * @return Key
     */
    String resolver(JoinPoint joinPoint, Idempotent idempotent);

}

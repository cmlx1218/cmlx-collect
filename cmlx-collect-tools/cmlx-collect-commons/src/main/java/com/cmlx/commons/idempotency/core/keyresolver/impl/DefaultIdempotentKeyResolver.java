package com.cmlx.commons.idempotency.core.keyresolver.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.cmlx.commons.idempotency.core.annotation.Idempotent;
import com.cmlx.commons.idempotency.core.keyresolver.IdempotentKeyResolver;
import org.aspectj.lang.JoinPoint;

/**
 * @author cmlx
 * @desc 默认幂等 Key 解析器，使用方法名 + 方法参数，组装成一个 Key，为了避免 Key 过长，使用 MD5 进行“压缩”
 * @date 2022/5/6 20:06
 */
public class DefaultIdempotentKeyResolver implements IdempotentKeyResolver {

    @Override
    public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
        String methodName = joinPoint.getSignature().toString();
        String argsStr = StrUtil.join(",", joinPoint.getArgs());
        return SecureUtil.md5(methodName + argsStr);
    }

}

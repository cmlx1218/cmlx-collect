package com.cmlx.commons.exception.extension;

import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:50
 * @Desc ->
 **/
public interface ServiceExceptionBuilder {
    public ServiceExceptionBuilder setThrowType(String throwType);

    public ServiceExceptionBuilder setCode(int code);

    public ServiceExceptionBuilder setFields(Map<String, String> fields);

    public ServiceExceptionBuilder setMessage(String message);

    public ServiceExceptionBuilder setMessageParameters(Map<String, Object> messageParameters);

    public Exception build();
}

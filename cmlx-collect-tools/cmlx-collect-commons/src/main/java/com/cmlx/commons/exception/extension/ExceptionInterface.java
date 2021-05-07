package com.cmlx.commons.exception.extension;

import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:49
 * @Desc -> 异常接口
 **/
public interface ExceptionInterface {
    /**
     * 错误定位
     *
     * @return
     */
    public String getThrowType();

    /**
     * 错误代码
     *
     * @return
     */
    public int getCode();

    /**
     * 错误参数及错误原因
     *
     * @return
     */
    public Map<String, String> getFields();

    /**
     * 错误消息模板
     *
     * @return
     */
    public String getMessageTemplate();

    /**
     * 消息模板参数
     *
     * @return
     */
    public Map<String, Object> getMessageParameters();

    /**
     * 异常消息
     *
     * @return
     */
    public String getExceptionMessage();
}


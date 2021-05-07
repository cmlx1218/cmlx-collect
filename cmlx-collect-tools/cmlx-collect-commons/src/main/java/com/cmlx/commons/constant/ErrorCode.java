package com.cmlx.commons.constant;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:57
 * @Desc ->
 **/
public enum ErrorCode {

    /**
     * 未登录
     */
    NotLogin(302),

    /**
     * 请求头Drivers格式错误
     */
    DriversHeader(306),

    /**
     * 解析URL失败
     */
    ErrorResolveUrl(347),

    /**
     * 资源未找到
     */
    ResourceNotFound(404),

    /**
     * 请求冲突或数据库唯一冲突
     */
    Conflict(301),

    /**
     * 提交的参数有异常
     */
    Parameter(300),

    /**
     * ok
     */
    OK(200),
    /**
     * 服务器异常
     */
    Server(500);

    private static final String PREFIX = "{com.cmlx.exception.";
    private static final String SUFFIX = ".message}";
    private int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getTemplate() {
        return PREFIX + code + SUFFIX;
    }
}

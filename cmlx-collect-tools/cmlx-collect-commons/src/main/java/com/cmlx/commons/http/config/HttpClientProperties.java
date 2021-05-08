package com.cmlx.commons.http.config;

import lombok.Data;

import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:08
 * @Desc ->
 **/
@Data
public class HttpClientProperties {
    private String host;
    private String v3Host;
    private Integer maxPreRoute;
    private Boolean https;
    private Map<String,String> urlMap;
}

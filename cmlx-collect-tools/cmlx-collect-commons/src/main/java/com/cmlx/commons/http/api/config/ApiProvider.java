package com.cmlx.commons.http.api.config;

import com.cmlx.commons.http.HttpClientManager;
import com.cmlx.commons.http.config.HttpClientProperties;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:07
 * @Desc ->
 **/
public interface ApiProvider {
    void setHttpClientProperties(HttpClientProperties properties);
    void setHttpClientManager(HttpClientManager clientManager);
    void init();
}

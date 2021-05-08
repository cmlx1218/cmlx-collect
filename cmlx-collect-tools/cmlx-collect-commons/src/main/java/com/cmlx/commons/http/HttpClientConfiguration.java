package com.cmlx.commons.http;

import com.cmlx.commons.http.api.annotation.Api;
import com.cmlx.commons.http.api.annotation.ApiScan;
import com.cmlx.commons.http.api.config.ApiProvider;
import com.cmlx.commons.http.config.HttpClientProperties;
import com.cmlx.commons.http.config.HttpManagerProperties;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:04
 * @Desc -> http请求工具类：com.cmlx.http(包路径)、cmlx.http(配置文件路径)
 **/
@Configuration
@ConditionalOnProperty(prefix = "cmlx.http", name = "enabled")
@EnableConfigurationProperties(HttpManagerProperties.class)
@ApiScan("com.cmlx.http")
public class HttpClientConfiguration {

    private HttpManagerProperties managerProperties;
    @Autowired(required = false)
    private List<ApiProvider> apiProviders;

    public HttpClientConfiguration(HttpManagerProperties managerProperties) {
        this.managerProperties = managerProperties;
    }

    @Bean
    public HttpClientManager httpClientManager() {
        HttpClientManager httpClientManager = new HttpClientManager(managerProperties);
        Map<String, HttpClientProperties> clientProperties = managerProperties.getClientProperties();
        if(!CollectionUtils.isEmpty(clientProperties)) {
            clientProperties.forEach((s, httpClientProperties) -> {
                HttpHost httpHost = HttpHost.create(httpClientProperties.getHost());
                httpClientManager.setMaxPerRoute(httpHost,httpClientProperties.getMaxPreRoute());
                ApiProvider apiProvider = findApiProvider(s);
                if(null != apiProvider) {
                    apiProvider.setHttpClientManager(httpClientManager);
                    apiProvider.setHttpClientProperties(httpClientProperties);
                    apiProvider.init();
                }
            });
        }
        return httpClientManager;
    }

    private ApiProvider findApiProvider(String name) {
        if(CollectionUtils.isEmpty(apiProviders)) return null;
        for(ApiProvider apiProvider:apiProviders) {
            Api annotation = AnnotationUtils.findAnnotation(apiProvider.getClass(), Api.class);
            String value = annotation.value();
            if(name.equals(value)) return apiProvider;
        }
        return null;
    }
}


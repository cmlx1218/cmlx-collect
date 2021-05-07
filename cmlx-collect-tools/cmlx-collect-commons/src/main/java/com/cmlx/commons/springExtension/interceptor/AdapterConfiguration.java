package com.cmlx.commons.springExtension.interceptor;

import com.cmlx.commons.springExtension.resolver.CurrentUserMethodArgumentResolver;
import com.cmlx.commons.springExtension.resolver.RequestModelMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 16:13
 * @Desc ->
 **/
@Configuration
public class AdapterConfiguration extends WebMvcConfigurerAdapter {
    /**
     * 参数映射解析器
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new RequestModelMethodArgumentResolver());
        argumentResolvers.add(new CurrentUserMethodArgumentResolver());
    }
}


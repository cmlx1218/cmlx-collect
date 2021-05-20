package com.cmlx.annotation.method.permission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @Author CMLX
 * @Date -> 2021/5/20 17:51
 * @Desc ->
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private PermissionCheckInterceptor permissionCheckInterceptor;

    //public static void main(String[] args) {
    //    SpringApplication.run(SuperrescueReportingApplication.class, args);
    //}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PermissionCheckInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(permissionCheckInterceptor);
        super.addInterceptors(registry);
    }
}

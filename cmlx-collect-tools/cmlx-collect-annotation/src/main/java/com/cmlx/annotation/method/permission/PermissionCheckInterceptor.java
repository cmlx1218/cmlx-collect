package com.cmlx.annotation.method.permission;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author CMLX
 * @Date -> 2021/5/20 17:17
 * @Desc ->
 **/
//@SuppressWarnings("ALL")
@Component
public class PermissionCheckInterceptor extends HandlerInterceptorAdapter {

    /**
     * 处理器处理之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PermissionCheck permissionCheck = findPermissionCheck(handlerMethod);

        // 如果没有添加权限注解则直接跳过允许访问
        if (permissionCheck == null) {
            return true;
        }

        //TODO 权限校验一般需要获取用户信息，通过查询数据库进行权限校验
        //TODO 这里只进行简单演示，如果resourceKey为testKey则校验通过，否则不通过
        // 获取注解中的值
        String resourceKey = permissionCheck.resourceKey();
        if ("testKey".equals(resourceKey)) {
            return true;
        }
        return false;
    }

    private PermissionCheck findPermissionCheck(HandlerMethod handlerMethod) {
        // 在方法上寻找注解
        PermissionCheck permissionCheck = handlerMethod.getMethodAnnotation(PermissionCheck.class);
        if (permissionCheck == null) {
            // 在类上寻找注解
            permissionCheck = handlerMethod.getBeanType().getAnnotation(PermissionCheck.class);
        }
        return permissionCheck;
    }
}

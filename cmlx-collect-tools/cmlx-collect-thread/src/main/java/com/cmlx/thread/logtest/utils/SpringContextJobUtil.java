package com.cmlx.thread.logtest.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:56
 * @Desc -> spring 上下文工具类，用于在spring容器启动后，使用getBean方法获取容器中的实例bean
 **/
@Component
public class SpringContextJobUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

}


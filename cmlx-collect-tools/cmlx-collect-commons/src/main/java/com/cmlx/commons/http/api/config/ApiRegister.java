package com.cmlx.commons.http.api.config;

import com.cmlx.commons.http.api.annotation.Api;
import com.cmlx.commons.http.api.annotation.ApiScan;
import com.cmlx.commons.support.ReflectionUtility;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.Set;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 11:07
 * @Desc ->
 **/
@Slf4j
public class ApiRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ApiScan.class.getName()));
        String scanPackage = annoAttrs.getString("value");
        try {
            Set<Class<?>> classes = ReflectionUtility.loadClassesByAnnotationClass(
                    Api.class, scanPackage.split(","));
            classes.forEach(aClass -> {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setSynthetic(true);
                beanDefinition.setBeanClass(aClass);
                registry.registerBeanDefinition(aClass.getSimpleName(),beanDefinition);
            });
        } catch (IOException e) {
            log.error("Api register error",e);
        } catch (ClassNotFoundException e) {
            log.error("Api register error",e);
        }
    }
}


package com.cmlx.commons.exception;


import com.cmlx.commons.exception.annotation.ExpTranslation;
import com.cmlx.commons.exception.annotation.TranslationScan;
import com.cmlx.commons.support.ReflectionUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.io.IOException;
import java.util.Set;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:46
 * @Desc ->
 **/
@Slf4j
public class TranslationRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(TranslationScan.class.getName()));
        String values = annoAttrs.getString("value");
        try {
            Set<Class<?>> classes = ReflectionUtility.loadClassesByAnnotationClass(
                    ExpTranslation.class, values.split(","));
            classes.forEach(aClass -> {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setSynthetic(true);
                beanDefinition.setBeanClass(aClass);
                registry.registerBeanDefinition(aClass.getSimpleName(),beanDefinition);
            });
        } catch (IOException e) {
            log.error("Exception translation register error",e);
        } catch (ClassNotFoundException e) {
            log.error("Exception translation register error",e);
        }
    }
}

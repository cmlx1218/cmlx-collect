package com.cmlx.annotation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cmlx"})
public class CmlxCollectAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmlxCollectAnnotationApplication.class, args);
    }

}

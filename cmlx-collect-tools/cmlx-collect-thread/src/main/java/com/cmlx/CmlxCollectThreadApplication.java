package com.cmlx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.cmlx.thread"})
public class CmlxCollectThreadApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmlxCollectThreadApplication.class, args);
    }

}

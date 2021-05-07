package com.cmlx.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cmlx"})
public class CollectTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectTestApplication.class, args);
    }

}

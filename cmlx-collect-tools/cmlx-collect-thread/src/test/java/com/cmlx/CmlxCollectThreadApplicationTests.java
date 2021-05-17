package com.cmlx;

import com.cmlx.thread.spring.service.ISerialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CmlxCollectThreadApplicationTests {

    @Autowired
    private ISerialService iSerialService;

    @Test
    void contextLoads() {
        iSerialService.executeAsync();
    }

}

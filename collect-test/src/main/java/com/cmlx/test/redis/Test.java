package com.cmlx.test.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author CMLX
 * @Date -> 2021/8/3 17:33
 * @Desc ->
 **/
@SpringBootTest
class Test {

    @Autowired
    private RedisUtils redisUtils;

    @org.junit.jupiter.api.Test
    void testRedis() {

        redisUtils.set("sex", "man");


    }

}

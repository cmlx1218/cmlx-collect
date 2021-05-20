package com.cmlx.annotation.method.permission;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author CMLX
 * @Date -> 2021/5/20 17:31
 * @Desc ->
 **/
@RestController
@RequestMapping("/annotation")
public class TestController {

    @RequestMapping("testPermission")
    @PermissionCheck(resourceKey = "test")
    public void testPermission() {
        System.out.println("测试通过");
    }

}

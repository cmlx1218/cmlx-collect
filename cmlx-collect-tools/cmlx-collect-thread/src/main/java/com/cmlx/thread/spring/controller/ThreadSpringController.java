package com.cmlx.thread.spring.controller;

import com.cmlx.thread.spring.service.IParallelService;
import com.cmlx.thread.spring.service.ISerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 18:13
 * @Desc ->
 **/
@RestController
@RequestMapping("/v1/api")
public class ThreadSpringController {

    @Autowired
    private ISerialService iSerialService;

    @Autowired
    private IParallelService iParallelService;

    @RequestMapping("/testSerial")
    public void testSerial() {
        iSerialService.executeAsync();
    }

    @RequestMapping("/testParallel")
    public void testParallel() throws ExecutionException, InterruptedException {
        iParallelService.testParallel();
    }

}

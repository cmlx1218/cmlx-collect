package com.cmlx.thread.ordinary.controller;

import com.cmlx.thread.ordinary.service.IParallelService;
import com.cmlx.thread.ordinary.service.ISerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 14:25
 * @Desc -> 串行改并行运行Controller
 **/
@RequestMapping("/v1/api/ordinary")
@RestController
public class ParallelController {

    @Autowired
    private ISerialService iSerialService;

    @Autowired
    private IParallelService iParallelService;

    @RequestMapping("/serial")
    public Long testSerial() {
        long startTime = System.currentTimeMillis();
        iSerialService.testSerial();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }


    @RequestMapping("/parallel")
    public Long testParallel() throws ExecutionException, InterruptedException {
        iParallelService.testParallel();
        return 1000L;
    }


}


package com.cmlx.thread.spring.service.impl;

import com.cmlx.thread.spring.service.IMainService;
import com.cmlx.thread.spring.service.IParallelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 18:03
 * @Desc ->
 **/
@Service
public class ParallelServiceImpl implements IParallelService {
    
    @Autowired
    private IMainService iMainService;
    
    @Override
    public void testParallel() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Future<String> stringFuture = iMainService.addAsync();
        Future<String> stringFuture1 = iMainService.subAsync();
        Future<String> stringFuture2 = iMainService.mulAsync();
        Future<String> stringFuture3 = iMainService.divAsync();
        String add = stringFuture.get();
        String sub = stringFuture1.get();
        String mul = stringFuture2.get();
        String div = stringFuture3.get();

        System.out.println("并行执行耗时：" + (System.currentTimeMillis() - startTime));
    }
}

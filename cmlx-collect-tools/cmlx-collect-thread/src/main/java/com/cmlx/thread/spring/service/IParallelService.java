package com.cmlx.thread.spring.service;

import java.util.concurrent.ExecutionException;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 18:03
 * @Desc ->
 **/
public interface IParallelService {

    void testParallel() throws ExecutionException, InterruptedException;

}

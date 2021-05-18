package com.cmlx.thread.ordinary.service;

import java.util.concurrent.ExecutionException;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 12:30
 * @Desc ->
 **/
public interface IParallelService {

    void testParallel() throws ExecutionException, InterruptedException;

}

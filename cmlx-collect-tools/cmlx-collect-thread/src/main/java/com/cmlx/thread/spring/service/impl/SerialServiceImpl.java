package com.cmlx.thread.spring.service.impl;

import com.cmlx.thread.spring.service.IMainService;
import com.cmlx.thread.spring.service.ISerialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 18:02
 * @Desc ->
 **/
@Slf4j
@Service
public class SerialServiceImpl implements ISerialService {

    @Autowired
    private IMainService iMainService;

    @Override
    @Async("asyncServiceExecutor")
    public void executeAsync() {
        log.info("start executeAsync");
        long startTime = System.currentTimeMillis();
        System.out.println("异步线程要做的事情");
        System.out.println("可以在这里执行批量插入等耗时的事情");
        iMainService.add();
        iMainService.sub();
        iMainService.mul();
        iMainService.div();

        log.info("end executeAsync, 用时=" + (System.currentTimeMillis() - startTime));
    }

}

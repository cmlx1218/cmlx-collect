package com.cmlx.thread.logtest.controller;

import com.cmlx.thread.logtest.logpool.LogPoolManager;
import com.cmlx.thread.logtest.persist.entity.LogBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 11:16
 * @Desc ->
 **/
@Slf4j
@RestController
@RequestMapping("/v1/api")
public class TestLogController {

    @Autowired
    private LogPoolManager logPoolManager;

    @RequestMapping("/log/test")
    public void logTest() {
        // 具体逻辑
        LogBean logBean = new LogBean();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        logBean.setLogContent("test log" + dateFormat.format(System.currentTimeMillis()));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logBean.setCreatets(format.format(System.currentTimeMillis()));
        // 将业务日志放入到队列中，然后使用线程 异步 批量进行入库，提升接口的响应速度
        try {
            logPoolManager.addLog(logBean);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info("log offer queue success !");
    }

}


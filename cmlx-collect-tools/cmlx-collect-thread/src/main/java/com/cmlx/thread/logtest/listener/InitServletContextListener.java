package com.cmlx.thread.logtest.listener;

import com.cmlx.thread.logtest.logclean.CleanManager;
import com.cmlx.thread.logtest.logpool.LogPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:20
 * @Desc -> 项目启动监听器，启动时进行数据初始化，以及启动各模块组件
 **/
@Slf4j
@WebListener
public class InitServletContextListener implements ServletContextListener {

    @Autowired
    private LogPoolManager logPoolManager;
    @Autowired
    private CleanManager cleanManager;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 日志 异步线程池处理 启动
        logPoolManager.init();
        log.info("日志异步池化处理启动成功!!!");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 关闭线程池
        logPoolManager.shutdown();
        cleanManager.shutdown();
    }
}


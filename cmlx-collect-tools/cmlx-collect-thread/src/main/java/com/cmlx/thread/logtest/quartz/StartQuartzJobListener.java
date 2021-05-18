package com.cmlx.thread.logtest.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:59
 * @Desc -> Scheduler注入到Spring容器，并采用spring容器加载完毕后的监听事件，启动定时任务
 **/
@Slf4j
@Configuration
public class StartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private SchedulerQuartzConfig quartzConfig;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            quartzConfig.startJob();
            log.info("定时任务启动成功");
        } catch (Exception e) {
            log.error("定时任务启动失败", e);
        }
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException {
        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
        return schedulerFactoryBean.getScheduler();
    }
}


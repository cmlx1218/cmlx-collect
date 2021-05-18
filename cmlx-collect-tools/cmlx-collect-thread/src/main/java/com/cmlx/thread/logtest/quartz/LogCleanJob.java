package com.cmlx.thread.logtest.quartz;

import com.cmlx.thread.logtest.logclean.CleanManager;
import com.cmlx.thread.logtest.utils.SpringContextJobUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:58
 * @Desc -> 日志清理 定时任务
 **/
@Slf4j
public class LogCleanJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Log Clean Quartz execute start . . . . . . .");
        //TODO 业务逻辑
        /**
         * 通过 自定义工具类 获取Spring容器中的实例bean
         * 在quartz框架中，Job 是通过反射出来的实例，不受spring的管理，即使使用Component注解将其标记位组件类，
         * 也不会被注册到容器中，所以无法直接通过自动注入IOC容器的中的对象
         */
        CleanManager cleanManager = (CleanManager) SpringContextJobUtil.getBean("cleanManager");
        // 进行日志清理
        cleanManager.cleanLogStart();
        log.info("Log Clean Quartz execute end . . . . . . . .");
    }
}

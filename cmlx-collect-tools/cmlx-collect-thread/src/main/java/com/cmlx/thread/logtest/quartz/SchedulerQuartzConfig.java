package com.cmlx.thread.logtest.quartz;

import cn.hutool.setting.dialect.Props;
import com.cmlx.thread.logtest.bean.JobBean;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:59
 * @Desc -> 定时任务 quartz 配置类
 **/
@Slf4j
@Configuration
public class SchedulerQuartzConfig {

    /**
     * 任务调度器
     */
    @Autowired
    private Scheduler scheduler;

    public void startJob() throws Exception {
        List<JobBean> jobBeanList = getJobBeanByConfig();
        if (jobBeanList != null && jobBeanList.size() > 0) {
            for (JobBean jobBean : jobBeanList) {
                registerJobToScheduler(scheduler, jobBean);
            }
        }
        scheduler.start();
    }

    /**
     * 获取定时任务配置参数
     *
     * @return
     */
    private List<JobBean> getJobBeanByConfig() throws Exception {
        Props props = new Props("application-quartz.properties");
        String jobList = props.getStr("jobList");
        if (jobList == null || "".equals(jobList)) {
            throw new Exception("定时任务集合 jobList 未配置");
        }
        List<JobBean> jobBeanList = new ArrayList<>();
        String[] jobs = jobList.split(",");

        for (int i = 0; i < jobs.length; i++) {
            JobBean jobBean = new JobBean();
            jobBean.setJobClass(props.getStr(jobs[i] + ".jobClass"));
            jobBean.setJobGroup(jobs[i]);
            jobBean.setJobName(props.getStr(jobs[i] + ".name"));
            jobBean.setCronExpression(props.getStr(jobs[i] + ".cron"));
            jobBeanList.add(jobBean);
        }
        return jobBeanList;
    }

    /**
     * 将定时任务与其对应的触发器注册到调度器Scheduler中
     *
     * @param scheduler
     * @param jobBean
     */
    private void registerJobToScheduler(Scheduler scheduler, JobBean jobBean) {
        try {
            Class<Job> jobClass = (Class<Job>) Class.forName(jobBean.getJobClass());

            // 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
            // JobDetail 是具体Job实例
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .withIdentity(jobBean.getJobName(), jobBean.getJobGroup()).build();

            // 基于表达式构建触发器执行时间配置
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(jobBean.getCronExpression());

            // CronTrigger表达式触发器 继承于Trigger
            // TriggerBuilder 用于构建《 触发器实例 》
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(jobBean.getJobName(), jobBean.getJobGroup())
                    .withSchedule(cronScheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, cronTrigger);

        } catch (Exception e) {
            log.error("\n定时任务注册到调度器失败", e);
        }
    }

}


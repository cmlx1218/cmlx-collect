package com.cmlx.thread.logtest.bean;

import lombok.Data;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:59
 * @Desc -> job 定时任务bean
 **/
@Data
public class JobBean {

    /**
     * 任务描述, 任务名
     */
    private String jobName;

    /**
     * 任务运行时间表达式
     */
    private String cronExpression;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务类的全路径
     */
    private String jobClass;

}
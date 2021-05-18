package com.cmlx.thread.logtest.service.impl;

import com.cmlx.thread.logtest.persist.entity.LogBean;
import com.cmlx.thread.logtest.persist.repository.LogBeanRepository;
import com.cmlx.thread.logtest.service.ITestLogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:48
 * @Desc ->
 **/
public class TestLogServiceImpl implements ITestLogService {

    @Autowired
    private LogBeanRepository logBeanRepository;

    @Override
    public void batchInsert(List<LogBean> list) {
        //TODO 插入日志数据到数据库
        for (LogBean logBean : list) {
            logBeanRepository.save(logBean);
        }
        System.out.println("插入日志数据到数据库");
    }

}

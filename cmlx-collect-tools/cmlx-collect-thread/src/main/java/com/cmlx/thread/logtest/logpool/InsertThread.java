package com.cmlx.thread.logtest.logpool;

import com.cmlx.thread.logtest.persist.entity.LogBean;
import com.cmlx.thread.logtest.service.ITestLogService;

import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:46
 * @Desc -> 日志批量插入 的工作线程
 **/
public class InsertThread implements Runnable{

    private ITestLogService iTestLogService;

    private List<LogBean> list;

    public InsertThread(ITestLogService iTestLogService, List<LogBean> list) {
        this.iTestLogService = iTestLogService;
        this.list = list;
    }

    @Override
    public void run() {
        iTestLogService.batchInsert(list);
        // GC
        list = null;
    }
}
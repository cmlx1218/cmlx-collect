package com.cmlx.thread.logtest.logclean;

import com.cmlx.thread.logtest.bean.LogCleanBean;
import com.cmlx.thread.logtest.service.ILogCleanService;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:53
 * @Desc -> 实际进行日志清理时的线程
 **/
public class CleanThread implements Runnable {

    private ILogCleanService iLogCleanService;

    private LogCleanBean logCleanBean;

    public CleanThread(ILogCleanService iLogCleanService, LogCleanBean logCleanBean) {
        this.iLogCleanService = iLogCleanService;
        this.logCleanBean = logCleanBean;
    }


    @Override
    public void run() {
        iLogCleanService.logBatchClean(logCleanBean);
        // GC
        logCleanBean = null;

    }
}

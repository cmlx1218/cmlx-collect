package com.cmlx.thread.logtest.logclean;

import cn.hutool.setting.dialect.Props;
import com.cmlx.thread.logtest.bean.LogCleanBean;
import com.cmlx.thread.logtest.service.ILogCleanService;
import com.cmlx.thread.logtest.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:52
 * @Desc -> 日志清理控制器
 **/
@Slf4j
@Component("cleanManager")
public class CleanManager {

    @Autowired
    private ILogCleanService iLogCleanService;

    private static ExecutorService cleanManagerThreadPool;

    private static Props props = new Props("application-cleanLog.properties");

    private static LogCleanBean logCleanBean = new LogCleanBean();

    static {
        // 获取配置的线程大小
        Integer threadNum = props.getInt("threads.pool.num");
        // 创建 固定线程数的线程池
        cleanManagerThreadPool = Executors.newFixedThreadPool(threadNum);
        // 每次清理日志时的数量
        logCleanBean.setBatchCleanCount(props.getInt("log.clean.batchCount"));
        // 清理的表
        logCleanBean.setTableName(props.getStr("log.clean.table"));
        // 根据日志清理的字段
        logCleanBean.setFieldName(props.getStr("log.clean.filed"));
        // 什么时间的日志可以被清理
        logCleanBean.setMinTime(DateUtil.getBeforeTime(props.getInt("log.clean.dateNum")));
    }


    /**
     * 多线程清理日志启动
     */
    public void cleanLogStart() {

        // 循环进行日志处理的次数
        Integer whileNum = props.getInt("log.clean.batchNum");
        LogCleanBean cleanBean = null;

        while (whileNum > 0) {

            // 查询符合每次删除数据量的时间段
            List<String> list = iLogCleanService.selectTime(logCleanBean);
            if (list != null && list.size() > 0) {
                cleanBean = new LogCleanBean();
                cleanBean.setTableName(logCleanBean.getTableName());
                cleanBean.setFieldName(logCleanBean.getFieldName());
                // 获取可以删除日志的最小生成时间
                cleanBean.setMinTime(list.get(list.size() - 1));
                // 获取可以删除日志的最大生成时间
                cleanBean.setMaxTime(list.get(0));
                logCleanBean.setMinTime(cleanBean.getMinTime());

                // 此次已经不满足设置的每次清理数据量，暂时不用清理
                if (list.size() < logCleanBean.getBatchCleanCount()) {
                    whileNum = 0;
                } else {
                    --whileNum;
                }
            } else {
                break;
            }
            // 进行多线程处理
            cleanManagerThreadPool.execute(new CleanThread(this.iLogCleanService, cleanBean));
        }
    }

    /**
     *  关闭 线程池
     */
    public void shutdown() {
        log.info("CleanManager Thread Pool shutdown...");
        /**
         *  关闭线程池
         *  调用了shutdownNow()方法后，线程池将不能接受新任务，也不会处理队列中的任务，并且会中断正在处理任务的线程
         */
        cleanManagerThreadPool.shutdownNow();
    }
}


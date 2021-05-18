package com.cmlx.thread.logtest.logpool;

import com.cmlx.thread.logtest.persist.entity.LogBean;
import com.cmlx.thread.logtest.service.ITestLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:21
 * @Desc ->
 **/
@Slf4j
@Component
@Scope("singleton")
public class LogPoolManager {

    @Autowired
    private ITestLogService iTestLogService;

    /**
     * 日志队列 的最大容量
     */
    private int MAX_QUEUE_SIZE = 100;

    /**
     * 日志批量插入的数量，10条日志
     */
    private int BATCH_SIZE = 10;

    /**
     * 线程睡眠时间，具体时间需要结合项目实际情况，单位毫秒
     */
    private int SLEEP_TIME = 500;

    /**
     * 日志插入执行的最大的时间间隔，单位毫秒
     */
    private long MAX_EXE_TiME = 5000;

    /**
     * 创建一个单线程的线程池,用来循环的监听队列中的日志数量以及决策什么时候将队列中的日志取出交由固定数线程的线程池做入库操作
     */
    private ExecutorService logManagerThreadPool = Executors.newSingleThreadExecutor();

    /**
     * 创建一个定长的线程池，线程数量为（java虚拟机可用的处理器数量 * 2 +20 ）
     */
    private ExecutorService logWorkerTheadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 20);

    /**
     * 任务队列，存放日志内容
     */
    private BlockingQueue<LogBean> queue;

    /**
     * Boolean 原子变量类
     */
    private AtomicBoolean run = new AtomicBoolean(true);

    /**
     * 整型 原子变量类，记录 任务队列 中的任务数量
     */
    private AtomicInteger logCount = new AtomicInteger(0);

    /**
     * 上一次执行日志插入时的时间
     */
    private long lastExecuteTime;

    /**
     * 初始化
     */
    public void init() {
        // 基于链表的双向阻塞队列，在队列的两端都可以插入和移除元素，是线程安全的，多线程并发下效率更高
        queue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);

        lastExecuteTime = System.currentTimeMillis();

        log.info("LogPoolManager init successfully...");

        logManagerThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                while (run.get()) {
                    try {
                        // 线程休眠，具体时间根据项目的时机情况配置
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException e) {
                        log.error("log manager Thread sleep fail", e);
                    }
                    // 满足存放了10个日志 或 满足时间间隔已经大于设置的最大时间间隔 执行日志插入
                    if (logCount.get() >= BATCH_SIZE || (System.currentTimeMillis() - lastExecuteTime) > MAX_EXE_TiME) {
                        if (logCount.get() > 0) {
                            log.info("begin drain log queue to database...");

                            List<LogBean> list = new ArrayList<>();
                            /**
                             * drainTo(): 一次性从BlockingQueue获取所有可用的数据对象(还可以指定获取数据的个数)
                             * 通过该方法，可以提升获取数据效率，不在需要多次分批枷锁或释放锁
                             * 将取出的数据放入指定的list集合中
                             */
                            queue.drainTo(list);
                            // 任务队列 中任务数量置为0
                            logCount.set(0);
                            // 从线程池中取出线程执行日志插入
                            logWorkerTheadPool.execute(new InsertThread(iTestLogService, list));
                            log.info("end drain log queue to database...");

                        }

                        // 获取当前执行的时间
                        lastExecuteTime = System.currentTimeMillis();
                    }
                }
                log.info("LogPoolManager shutdown successfully");
            }
        });
    }


    /**
     * 把日志放到队列中
     *
     * @param LogBean
     * @throws Exception
     */
    public void addLog(LogBean LogBean) throws Exception {
        if (logCount.get() >= MAX_QUEUE_SIZE) {
            // 当队列满时，直接将日志丢弃，并抛出异常
            throw new Exception("rejected .. Log count exceed log queue's max size ！");
        }
        // 将日志放入 任务队列中，放入成功返回true
        this.queue.offer(LogBean);
        // 队列中任务数量 +1
        logCount.incrementAndGet();
    }

    public void shutdown() {
        log.info("LogPoolManager Thread Pool shutdown...");
        // 结束while循环
        run.set(false);
        // 关闭线程池
        logWorkerTheadPool.shutdownNow();
        logManagerThreadPool.shutdownNow();
    }

}

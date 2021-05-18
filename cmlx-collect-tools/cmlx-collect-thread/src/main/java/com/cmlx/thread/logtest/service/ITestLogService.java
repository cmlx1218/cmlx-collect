package com.cmlx.thread.logtest.service;

import com.cmlx.thread.logtest.persist.entity.LogBean;

import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:47
 * @Desc ->
 **/
public interface ITestLogService {

    /**
     * 批量进行日志插入
     *
     * @param list
     */
    void batchInsert(List<LogBean> list);

}


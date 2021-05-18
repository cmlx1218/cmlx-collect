package com.cmlx.thread.logtest.service;

import com.cmlx.thread.logtest.bean.LogCleanBean;

import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:53
 * @Desc ->
 **/
public interface ILogCleanService {

    /**
     * 查询出符合删除量的 时间间隔
     *
     * @param logCleanBean
     * @return
     */
    List<String> selectTime(LogCleanBean logCleanBean);

    /**
     * 根据时间间隔进行删除
     *
     * @param logCleanBean
     */
    void logBatchClean(LogCleanBean logCleanBean);

}

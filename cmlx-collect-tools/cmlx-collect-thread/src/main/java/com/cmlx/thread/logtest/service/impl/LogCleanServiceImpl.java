package com.cmlx.thread.logtest.service.impl;

import com.cmlx.thread.logtest.bean.LogCleanBean;
import com.cmlx.thread.logtest.service.ILogCleanService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:53
 * @Desc ->
 **/
@Service
public class LogCleanServiceImpl implements ILogCleanService {

    @Override
    public List<String> selectTime(LogCleanBean logCleanBean) {
        //TODO 查询出符合删除量的 时间间隔

        return null;
    }

    @Override
    public void logBatchClean(LogCleanBean logCleanBean) {
        //TODO 批量清除日志

    }
}

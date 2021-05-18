package com.cmlx.thread.logtest.bean;

import lombok.Data;
import lombok.ToString;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:54
 * @Desc -> 日志清理的Bean
 **/
@Data
@ToString
public class LogCleanBean {

    /**
     *  需要删除的表
     */
    private String tableName;

    /**
     *  根据的哪个字段进行的删除，一般都是日期型字段
     */
    private String fieldName;

    /**
     *  每次删除的数量
     */
    private int batchCleanCount;

    /**
     *  根据每次删除的数据量，先查询出的符合在这个数据量的时间间隔的最小时间
     */
    private String minTime;

    /**
     *  根据每次删除的数据量，先查询出的符合在这个数据量的时间间隔的最大时间
     */
    private String maxTime;

}


package com.cmlx.thread.logtest.persist.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:41
 * @Desc ->
 **/
@Data
@Entity
@Table(name = "t_test_log")
public class LogBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String logContent;

    private String createts;

}

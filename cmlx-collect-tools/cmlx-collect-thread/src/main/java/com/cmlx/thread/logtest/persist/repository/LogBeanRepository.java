package com.cmlx.thread.logtest.persist.repository;

import com.cmlx.thread.logtest.persist.entity.LogBean;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author CMLX
 * @Date -> 2021/5/18 10:49
 * @Desc ->
 **/
public interface LogBeanRepository extends JpaRepository<LogBean, Integer> {
}


package com.cmlx.redis.service;

import com.cmlx.commons.support.JsonUtility;
import com.cmlx.commons.support.SpringContextUtility;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:19
 * @Desc ->
 **/
public abstract class BaseService<S> {

    protected S proxy;
    @Autowired
    protected SpringContextUtility springContextUtility;

    @PostConstruct
    protected void post() {
        Class<?>[] interfaces = this.getClass().getInterfaces();
        if(null == interfaces || interfaces.length == 0) return;
        proxy = (S) springContextUtility.getBean(interfaces[0]);
    }

    protected JsonNode getNodeList(String json) throws Exception {
        JsonNode node = JsonUtility.readTree(json);
        return node.get("list");
    }
}


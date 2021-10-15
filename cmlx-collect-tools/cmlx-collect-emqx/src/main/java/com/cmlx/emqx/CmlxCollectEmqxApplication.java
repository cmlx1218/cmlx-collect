package com.cmlx.emqx;

import com.cmlx.commons.constant.Constant;
import com.cmlx.emqx.config.EmqClient;
import com.cmlx.emqx.config.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CmlxCollectEmqxApplication {

    @Autowired
    private EmqClient emqClient;
    @Autowired
    private MqttProperties mqttProperties;

    public static void main(String[] args) {
        SpringApplication.run(CmlxCollectEmqxApplication.class, args);
    }

    @PostConstruct
    public void init() {
        emqClient.connect(mqttProperties.getUsername(), mqttProperties.getPassword());
        //订阅某一主题
        emqClient.subscribe("testtopic/#", Constant.QosEnum.QoS2);
        ////开启一个新的线程向该主题发送消息
        //new Thread(() -> {
        //    while (true) {
        //        emqClient.publish("testtopic/123", "mqtt msg:" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), QosEnum.QoS2, false);
        //        try {
        //            TimeUnit.SECONDS.sleep(5);
        //        } catch (InterruptedException e) {
        //            e.printStackTrace();
        //        }
        //    }
        //}).start();
    }

}

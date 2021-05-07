package com.cmlx.commons;

import com.cmlx.commons.encrypt.EncryptorUtility;
import com.cmlx.commons.generator.CodeGenerator;
import com.cmlx.commons.generator.GMERoomIdGenerator;
import com.cmlx.commons.generator.IdGenerator;
import com.cmlx.commons.support.BeanValidatorUtility;
import com.cmlx.commons.support.HostAddressUtility;
import com.cmlx.commons.support.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Set;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:07
 * @Desc ->
 **/
@Configuration
public class AutowiredConfiguration {

    @Autowired
    private Environment environment;

    /**
     * ID生成工具
     *
     * @return
     * @throws SocketException
     */
    @Bean
    public IdGenerator idGenerator() throws SocketException {
        String ignoreHost = environment.getProperty("cmlx.autowired.id-generator");
        if (StringUtility.hasText(ignoreHost)) {
            return new IdGenerator(hostToLong());
        }
        return new IdGenerator(HostAddressUtility.localHostAfterTwo());
    }

    /**
     * 订单编号生成
     *
     * @return
     * @throws SocketException
     */
    @Bean
    public CodeGenerator orderGenerator() throws SocketException {
        String ignoreHost = environment.getProperty("cmlx.autowired.id-generator");
        if (StringUtility.hasText(ignoreHost)) {
            return new CodeGenerator(hostToLong());
        }
        return new CodeGenerator(HostAddressUtility.localHostAfterTwo());
    }

    private long hostToLong() throws SocketException {
        String ignoreHost = environment.getProperty("cmlx.autowired.id-generator");
        if (StringUtility.hasText(ignoreHost)) {
            String[] ignoreHosts = ignoreHost.split(",");
            Set<InetAddress> hosts = HostAddressUtility.multiGetLocalAddress(ignoreHosts);
            if (!CollectionUtils.isEmpty(hosts)) {
                long lastTwoHost = HostAddressUtility.localHostAfterTwo((InetAddress) hosts.toArray()[0]);
                return lastTwoHost;
            }
        }
        return HostAddressUtility.localHostAfterTwo();
    }

    /**
     * MD5，SHA1，SHA256，SHA384，SHA512
     *
     * @return
     */
    @Bean
    public EncryptorUtility encryptorUtility() {
        String encryptor = environment.getProperty("cmlx.autowired.encryptor");
        if (StringUtility.hasText(encryptor)) {
            return new EncryptorUtility(encryptor);
        }
        return new EncryptorUtility("");
    }


    @Bean
    public BeanValidatorUtility beanValidatorUtility(ApplicationContext context) {
        return new BeanValidatorUtility(context);
    }

    @Bean
    public GMERoomIdGenerator gmeRoomIdGenerator() throws Exception {
        /*String defaultEncryptKey = environment.getProperty("appserver.default-encrypt-key");
        if (StringUtility.isEmpty(defaultEncryptKey)) {
            throw EXPF.exception(ErrorCode.DataException.getCode(), "appserver.default-encrypt-key配置无法找到", true);
        }*/
        return new GMERoomIdGenerator("P7W3973628");
    }
}


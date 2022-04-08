package com.cmlx.commons.excel.easynew.config;

import com.cmlx.commons.excel.easynew.service.IDictDataService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cmlx
 * @desc
 * @date 2022/4/6 11:38
 */
@Configuration
public class DictDataConfiguration {

    @Bean
    public DictDataUtils dictDataUtils(IDictDataService service) {
        DictDataUtils.init(service);
        return new DictDataUtils();
    }

}

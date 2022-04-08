package com.cmlx.commons.excel.easynew.annotation;

import java.lang.annotation.*;

/**
 * @author cmlx
 * @desc 字典格式化，将字典数据的值，格式化成字典数据的标签
 * @date 2022/4/2 16:26
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DictFormat {

    /**
     * @return 字典类型
     */
    String value();

}

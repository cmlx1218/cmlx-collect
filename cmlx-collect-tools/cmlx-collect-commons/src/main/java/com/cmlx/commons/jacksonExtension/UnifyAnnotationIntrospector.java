package com.cmlx.commons.jacksonExtension;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 11:05
 * @Desc -> 扩展Jackson注解查询，重写findFilterId方法。
 *          当查询FilterId时，先获取JsonFilter注解key，如果没有获取到
 *          直接使用对象Class当做FilterId使用
 **/
public class UnifyAnnotationIntrospector extends JacksonAnnotationIntrospector {

    @Override
    public Object findFilterId(Annotated a) {
        Object filterId = super.findFilterId(a);
        if (null == filterId) {
            return a.getRawType();
        }
        return filterId;
    }
}
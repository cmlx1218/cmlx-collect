package com.cmlx.commons.springExtension.view;

import com.cmlx.commons.module.PropertyFilterInfo;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 10:37
 * @Desc -> 查询成功返回包装
 **/
public class UnifySuccessView extends UnifyView {

    public UnifySuccessView() {
        this(null);
    }

    public UnifySuccessView(Object object) {
        super();
        if(null != object){
            this.addStaticAttribute(DATA,object);
        }
        this.addStaticAttribute(CODE,200);
    }

    public UnifySuccessView(Object object, PropertyFilterInfo... filters) {
        super(filters);
        if(null != object){
            this.addStaticAttribute(DATA,object);
        }
        this.addStaticAttribute(CODE,200);
    }
}

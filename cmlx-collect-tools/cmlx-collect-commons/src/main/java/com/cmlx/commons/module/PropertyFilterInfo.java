package com.cmlx.commons.module;

import lombok.Data;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 11:07
 * @Desc -> 过滤信息,当class为null时，将设置为全局过滤
 **/
@Data
public class PropertyFilterInfo {
    private Class<?> _clazz;
    private boolean _include;
    private String[] _properties;

    public PropertyFilterInfo(String... properties) {
        this(true, properties);
    }

    public PropertyFilterInfo(boolean include, String... properties) {
        this(include, null, null == properties ? new String[0] : properties);
    }

    public PropertyFilterInfo(boolean include, Class<?> clazz, String... properties) {
        this._clazz = clazz;
        this._include = include;
        this._properties = null == properties ? new String[0] : properties;
    }
}

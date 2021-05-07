package com.cmlx.commons.module;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.PropertyDescriptor;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:24
 * @Desc -> 对象属性信息类
 **/
@Data
@AllArgsConstructor
public class EntityPropertyInfo {
    private PropertyDescriptor primaryKey;
    private PropertyDescriptor[] otherKey;
}


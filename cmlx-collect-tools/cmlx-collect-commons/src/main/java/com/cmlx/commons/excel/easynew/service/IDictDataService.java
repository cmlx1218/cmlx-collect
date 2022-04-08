package com.cmlx.commons.excel.easynew.service;


import com.cmlx.commons.excel.easynew.entity.DictDataEntity;

/**
 * @author cmlx
 * @desc 字典类型, 使用的服务实现这个接口即可
 * @date 2022/4/6 9:22
 */
public interface IDictDataService {
    DictDataEntity getValueByTypeAndLabel(String type, String label);

    DictDataEntity getLabelByTypeAndValue(String type, String value);
}

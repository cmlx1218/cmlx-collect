package com.cmlx.commons.excel.easynew.config;

import com.cmlx.commons.excel.easynew.constant.DictTypeConstant;
import com.cmlx.commons.excel.easynew.entity.DictDataEntity;
import com.cmlx.commons.excel.easynew.service.IDictDataService;
import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author cmlx
 * @desc 字典转换
 * @date 2022/4/6 11:33
 */
@Slf4j
public class DictDataUtils {

    private static IDictDataService service;

    public static void init(IDictDataService service) {
        DictDataUtils.service = service;
        log.info("[init][初始化 DictDataUtils 成功]");
    }


    public static DictDataEntity getValueByTypeAndLabel(String type, String label) {
        // 特殊处理百分号转小数
        if (type.equals(DictTypeConstant.PercentConversion)) {
            DictDataEntity dictDataEntity = new DictDataEntity();
            try {
                dictDataEntity.setValue(NumberFormat.getPercentInstance().parse(label).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dictDataEntity;
        }
        return service.getValueByTypeAndLabel(type, label);
    }

    public static DictDataEntity getLabelByTypeAndValue(String type, String value) {
        // 特殊处理小数转百分号
        if (type.equals(DictTypeConstant.PercentConversion)) {
            DictDataEntity dictDataEntity = new DictDataEntity();
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMaximumFractionDigits(2); // 保留小数两位
            String format = percentInstance.format(Double.parseDouble(value)); //
            dictDataEntity.setLabel(format);
            return dictDataEntity;
        }
        return service.getLabelByTypeAndValue(type, value);
    }
}

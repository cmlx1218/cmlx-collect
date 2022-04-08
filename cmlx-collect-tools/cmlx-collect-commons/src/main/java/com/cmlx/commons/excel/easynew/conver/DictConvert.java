package com.cmlx.commons.excel.easynew.conver;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;
import org.sfsj.modules.hr.insurance.entity.DictDataEntity;
import org.sfsj.modules.hr.insurance.util.excel.annotation.DictFormat;
import org.sfsj.modules.hr.insurance.util.excel.config.DictDataUtils;

/**
 * @author cmlx
 * @desc 数据字典转换器 {@link DictDataEntity}
 * @date 2022/4/2 16:27
 */
@Slf4j
public class DictConvert implements Converter<Object> {

    @Override
    public Class<?> supportJavaTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public Object convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) {
        // 使用字典解析
        String type = getType(contentProperty);
        String label = cellData.getStringValue();
        //TODO 根据type和label获取字典值
        DictDataEntity entity = DictDataUtils.getValueByTypeAndLabel(type, label);
        if (entity == null) {
            log.error("[convertToJavaData][type({}) 解析不掉 label({})]", type, label);
            return null;
        }
        // 将 String 的 value 转换成对应的属性
        Class<?> fieldClazz = contentProperty.getField().getType();
        return Convert.convert(fieldClazz, entity.getValue());
    }

    @Override
    public CellData<String> convertToExcelData(Object object, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        // 空时，返回空
        if (object == null) {
            return new CellData<>("");
        }

        // 使用字典格式化
        String type = getType(contentProperty);
        String value = String.valueOf(object);
        //TODO 根据type和value获取标签值
        DictDataEntity entity = DictDataUtils.getLabelByTypeAndValue(type, value);

        if (entity == null) {
            log.error("[convertToExcelData][type({}) 转换不了 label({})]", type, value);
            return new CellData<>("");
        }
        // 生成 Excel 小表格
        return new CellData<>(entity.getLabel());
    }

    private static String getType(ExcelContentProperty contentProperty) {
        return contentProperty.getField().getAnnotation(DictFormat.class).value();
    }

}


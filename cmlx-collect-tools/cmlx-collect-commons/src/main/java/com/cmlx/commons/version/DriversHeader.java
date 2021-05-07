package com.cmlx.commons.version;

import com.cmlx.commons.constant.Constant;
import com.cmlx.commons.hibernateExtension.annotation.EnumValue;
import com.cmlx.commons.hibernateExtension.annotation.Version;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:32
 * @Desc ->
 **/
@Data
public class DriversHeader {
    @NotNull
    @EnumValue(clazz = Constant.DriversOs.class)
    private Integer os;         // 设备平台：1：ios    0:Android
    @NotBlank
    @Version
    private String clientVersion;     // app版本

    private String channel;    //下载渠道        0=appStore           安卓的下载渠道由客户端直接上报
}

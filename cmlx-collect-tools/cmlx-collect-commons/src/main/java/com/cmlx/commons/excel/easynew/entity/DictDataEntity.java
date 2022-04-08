package com.cmlx.commons.excel.easynew.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cmlx
 * @desc 字典数据
 * @date 2022/4/6 9:24
 */
@Data
//@TableName("hr_system_dict_data")
@Accessors(chain = false)
@EqualsAndHashCode(callSuper = false)
public class DictDataEntity implements Serializable {

//    @TableId(type = IdType.AUTO)
    private Long id;            // 主键id
    private String label;       // 字典标签
    private String value;       // 字典值
    private String dictType;    // 字典类型
    private String createBy;    // 创建人
    private Integer status;     // 状态
    private Date createTime;    // 创建时间
    private Date updateTime;    // 更新时间

}

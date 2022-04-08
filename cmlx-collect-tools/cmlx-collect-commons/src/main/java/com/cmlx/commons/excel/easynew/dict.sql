-- 2022-4-2 字典数据表
DROP TABLE IF EXISTS `hr_system_dict_data`;
CREATE TABLE hr_system_dict_data (
    id bigint AUTO_INCREMENT NOT NULL COMMENT '主键',
    label varchar(100) NOT NULL DEFAULT '' COMMENT '标签',
    value varchar(100) NOT NULL DEFAULT '' COMMENT '数值',
    dict_type varchar(100) NOT NULL DEFAULT '' COMMENT '字典类型',
    create_by varchar(36) DEFAULT NULL COMMENT '创建人',
    status tinyint NOT NULL DEFAULT '2' COMMENT '字典数据状态 0-无效 1-删除 2-正常',
    create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT '字典数据表';

-- 2022-4-6 字典数据表填充数据
BEGIN;
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('居民身份证（户口簿）','0','credentials_type','cmlx');
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('港澳台通行证','1','credentials_type','cmlx');
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('大额医疗费用补助','0','insurance_type','cmlx');
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('职工基本医疗保险','1','insurance_type','cmlx');
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('在职','0','insurance_retirement_sign','cmlx');
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('正常应缴','0','payment_type','cmlx');
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('补收','1','payment_type','cmlx');
INSERT INTO `hr_system_dict_data` (label,value,dict_type,create_by) VALUES ('足额缴纳','0','payment_sign','cmlx');
COMMIT;

package com.cmlx.commons.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author CMLX
 * @Date -> 2021/5/8 10:21
 * @Desc ->
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private String name;
    private Integer age;
    private String school;

}

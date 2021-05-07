package com.cmlx.commons.qrcode;

import com.cmlx.commons.constant.Constant;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:41
 * @Desc ->
 **/
@Data
@Accessors(chain = true)
public class JoinGroupDto {

    private Long groupId;
    private Long creatorId;
    private Constant.GroupNotificationType joinType;

}
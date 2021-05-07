package com.cmlx.commons.qrcode;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:47
 * @Desc ->
 **/
@Data
@Accessors(chain = true)
public class UrlInfoDto {

    private Long targetId;

    private String redirectUrl;

    private String shareUrl;

    private Long creatorId;

}

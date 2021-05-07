package com.cmlx.commons.jsoup;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 17:59
 * @Desc ->
 **/
@Data
@Accessors(chain = true)
public class UrlDto {

    private String url;
    private String urlTitle;
    private String urlImage;

}


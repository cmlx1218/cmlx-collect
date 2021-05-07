package com.cmlx.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:42
 * @Desc ->
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorBody {
    private int code;
    private String message;
    private String throwType;
}

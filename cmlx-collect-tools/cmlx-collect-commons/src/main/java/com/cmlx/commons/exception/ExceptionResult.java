package com.cmlx.commons.exception;

import com.cmlx.commons.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 15:03
 * @Desc ->
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResult {

    private int Code;

    private String msg;

    private Object data;

    public static ExceptionResult error(String msg){
        return new ExceptionResult(ErrorCode.Parameter.getCode(),msg,null);
    }

}

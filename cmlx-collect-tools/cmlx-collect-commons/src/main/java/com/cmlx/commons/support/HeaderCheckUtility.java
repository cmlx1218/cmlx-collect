package com.cmlx.commons.support;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.EXPF;
import com.cmlx.commons.version.DriversHeader;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:30
 * @Desc -> 请求头检测工具
 **/
@UtilityClass
public class HeaderCheckUtility {

    public final String HEADER_AIMY_TOKEN = "cmlx-token";
    public final String HEADER_AIMY_DRIVERS = "cmlx-drivers";
    public final String HEADER_AIMY_ID = "cmlx-id";

    public Exception checkAimyToken(String token) {
        if(!StringUtility.hasText(token)) {
            return  EXPF.exception(ErrorCode.NotLogin,false);
        }
        return null;
    }

    /**
     * 检查 cmlx-drivers
     *
     * @param drivers
     * @return
     * @throws Exception 300,306,404,
     */
    public DriversHeader checkAimyDrivers(String drivers) throws Exception {
        if(!StringUtility.hasText(drivers)) {
            Map<String,Object> messagePar = new HashMap<>();
            messagePar.put("type","header ["+HEADER_AIMY_DRIVERS+"]");
            throw EXPF.E404(messagePar,false);
        }
        DriversHeader header;
        try {
            header = JsonUtility.toObject(drivers, DriversHeader.class);
        } catch (Exception e) {
            throw EXPF.exception(ErrorCode.DriversHeader,false);
        }
        Set<ConstraintViolation<Object>> constraintViolations = BeanValidatorUtility.validBean(header);
        if(!CollectionUtils.isEmpty(constraintViolations)) {
            Map<String,String> error = new HashMap<>();
            constraintViolations.forEach(violation -> {
                String s = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                error.put(HEADER_AIMY_DRIVERS + "." +s,message);
            });
            throw EXPF.E300(error,false);
        }
        return header;
    }

    public DriversHeader getDriversHeader(HttpServletRequest request) throws Exception {
        Object attribute = request.getHeader(HEADER_AIMY_DRIVERS);
        if(null != attribute) return JsonUtility.toObject(attribute.toString(),DriversHeader.class);
        return null;
    }
}


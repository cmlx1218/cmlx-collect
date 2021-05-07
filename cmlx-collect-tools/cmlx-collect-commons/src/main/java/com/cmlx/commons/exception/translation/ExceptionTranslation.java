package com.cmlx.commons.exception.translation;

import com.cmlx.commons.exception.TranslationContext;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:53
 * @Desc -> 异常转换器接口
 **/
public interface ExceptionTranslation {
    /**
     * 检查是否支持
     *
     * @param ex
     * @return
     */
    boolean support(Exception ex);

    /**
     * 转换成JSON
     *
     * @param context
     * @return
     */
    AbstractView translationToJson(TranslationContext context);
}

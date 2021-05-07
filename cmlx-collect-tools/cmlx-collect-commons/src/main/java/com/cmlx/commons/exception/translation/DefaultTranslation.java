package com.cmlx.commons.exception.translation;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.TranslationContext;
import com.cmlx.commons.springExtension.view.UnifyFailureView;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 14:59
 * @Desc -> 默认异常转换器，输出Code=500
 **/
public class DefaultTranslation extends AbstractExceptionTranslation {
    @Override
    public boolean support(Exception ex) {
        return true;
    }

    @Override
    public AbstractView translationToJson(TranslationContext context) {
        AbstractView view = new UnifyFailureView();
        view.addStaticAttribute(CODE, ErrorCode.Server.getCode());
        view.addStaticAttribute(THROWTYPE,getThrowtype(context.getException()));
        view.addStaticAttribute(MESSAGE,interpolate(ErrorCode.Server.getTemplate(),null));
        view.addStaticAttribute(DETAILMESSAGE, context.getException().getLocalizedMessage());
        return view;
    }
}

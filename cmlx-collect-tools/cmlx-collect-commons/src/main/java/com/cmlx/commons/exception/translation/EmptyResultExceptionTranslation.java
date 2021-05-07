package com.cmlx.commons.exception.translation;

import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.exception.TranslationContext;
import com.cmlx.commons.exception.annotation.ExpTranslation;
import com.cmlx.commons.springExtension.view.UnifyFailureView;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 15:00
 * @Desc ->
 **/
@ExpTranslation
public class EmptyResultExceptionTranslation extends AbstractExceptionTranslation {

    @Override
    public boolean support(Exception ex) {
        return ex instanceof EmptyResultDataAccessException;
    }

    @Override
    public AbstractView translationToJson(TranslationContext context) {
        AbstractView view = new UnifyFailureView();
        view.addStaticAttribute(CODE, ErrorCode.ResourceNotFound.getCode());
        view.addStaticAttribute(THROWTYPE, getThrowtype(context.getException()));
        view.addStaticAttribute(MESSAGE, interpolate(ErrorCode.ResourceNotFound.getTemplate(),null));
        return view;
    }
}

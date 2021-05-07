package com.cmlx.commons.exception.translation;

import com.cmlx.commons.exception.TranslationContext;
import com.cmlx.commons.exception.annotation.ExpTranslation;
import com.cmlx.commons.exception.extension.ExceptionInterface;
import com.cmlx.commons.springExtension.view.UnifyFailureView;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 15:00
 * @Desc ->
 **/
@ExpTranslation
public class RepositoryThrowTranslation extends AbstractExceptionTranslation {
    @Override
    public boolean support(Exception ex) {
        boolean b = ex instanceof InvalidDataAccessApiUsageException;
        if(b) {
            Throwable cause = ex.getCause();
            return cause instanceof ExceptionInterface;
        }
        return false;
    }

    @Override
    public AbstractView translationToJson(TranslationContext context) {
        ExceptionInterface anInterface = (ExceptionInterface) context.getException().getCause();
        AbstractView view = new UnifyFailureView();
        view.addStaticAttribute(CODE, anInterface.getCode());
        view.addStaticAttribute(THROWTYPE, anInterface.getThrowType());
        String messageTemplate = anInterface.getMessageTemplate();
        MessageInterpolatorContext interpolatorContext = createInterpolatorContext(anInterface.getMessageParameters());
        if (StringUtils.hasText(messageTemplate)) {
            view.addStaticAttribute(MESSAGE, interpolateByMIContext(messageTemplate, interpolatorContext));
        } else {
            view.addStaticAttribute(MESSAGE, anInterface.getExceptionMessage());
        }
        Map<String, String> fields = anInterface.getFields();
        if (null != fields) {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                String value = entry.getValue();
                entry.setValue(interpolateByMIContext(value, interpolatorContext));
            }
        }
        view.addStaticAttribute(FIELDS, fields);
        return view;
    }
}

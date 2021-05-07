package com.cmlx.commons.exception.translation;

import com.cmlx.commons.exception.TranslationContext;
import com.cmlx.commons.exception.annotation.ExpTranslation;
import com.cmlx.commons.exception.extension.ExceptionInterface;
import com.cmlx.commons.springExtension.view.UnifyFailureView;
import com.cmlx.commons.support.StringUtility;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.springframework.web.servlet.view.AbstractView;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 15:01
 * @Desc ->
 **/
@ExpTranslation
public class ServiceExceptionTranslation extends AbstractExceptionTranslation {

    @Override
    public boolean support(Exception ex) {
        if (ex instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) ex;
            Throwable undeclaredThrowable = exception.getUndeclaredThrowable();
            ex = (Exception) undeclaredThrowable;
        }
        return ex instanceof ExceptionInterface;
    }

    @Override
    public AbstractView translationToJson(TranslationContext context) {
        ExceptionInterface anInterface;
        if (context.getException() instanceof UndeclaredThrowableException) {
            UndeclaredThrowableException exception = (UndeclaredThrowableException) context.getException();
            Throwable undeclaredThrowable = exception.getUndeclaredThrowable();
            anInterface = (ExceptionInterface) undeclaredThrowable;
        } else {
            anInterface = (ExceptionInterface) context.getException();
        }
        AbstractView view = new UnifyFailureView();
        view.addStaticAttribute(CODE, anInterface.getCode());
        String throwType = anInterface.getThrowType();
        if(StringUtility.isEmpty(throwType)) {
            throwType = getThrowtype((Exception) anInterface);
        }
        view.addStaticAttribute(THROWTYPE, throwType);
        String messageTemplate = anInterface.getMessageTemplate();
        MessageInterpolatorContext interpolatorContext = createInterpolatorContext(anInterface.getMessageParameters());
        if (StringUtility.hasText(messageTemplate)) {
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


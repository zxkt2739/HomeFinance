package com.example.core.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化工具类
 * @author faker
 */
@Component
public class I18nUtils {

    private static MessageSource messageSource;

    public I18nUtils(MessageSource messageSource) {
        I18nUtils.messageSource = messageSource;
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey, Locale locale) {
        try {
            return messageSource.getMessage(msgKey, null, locale);
        } catch (Exception e) {
            return msgKey;
        }
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey, Locale locale, String message) {
        try {
            String returnMessage = messageSource.getMessage(msgKey, null, locale);
            if (message != null) {
                return returnMessage + message;
            }
            return returnMessage;
        } catch (Exception e) {
            return msgKey;
        }
    }

}

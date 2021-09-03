package com.core.cryptolib.factories;


import com.core.cryptolib.CryptoLoggerService;
import org.json.simple.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class ResponsePrepareFactory extends JSONObject {

    public static JSONObject responseMessageFormat(HttpStatus status, String messageKey, Object[] objs, MessageSource messageSource) {
        JSONObject message = new JSONObject();

        message.put("detail", messageSource.getMessage(messageKey,
                objs,
                LocaleContextHolder.getLocale()));
        message.put("title", status.name());
        message.put("code", status.value());

//        CryptoLoggerService logger = new CryptoLoggerService(
//                "Микросервис",
//                "GlobalControllerExceptionHandler");

//        logger.info("responseMessageFormat",
//                message.toJSONString());

        return message;
    }

    public static JSONObject responseMessageFormat(HttpStatus status, String textMessage) {
        JSONObject message = new JSONObject();

        message.put("detail", textMessage);
        message.put("title", status.name());
        message.put("code", status.value());

//        CryptoLoggerService logger = new CryptoLoggerService(
//                "Микросервис",
//                "GlobalControllerExceptionHandler");
//
//        logger.info("responseMessageFormat",
//                message.toJSONString());

        return message;
    }
}

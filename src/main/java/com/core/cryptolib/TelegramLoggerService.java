/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author SAMS
 */
public class TelegramLoggerService {

    private final String channel;

    private final String token;

    private final String appName;

    private String className;
    
    boolean isDebug;

    public void setClassName(String className) {
        this.className = className;
    }

    public TelegramLoggerService(String channel, String token, String appName, String className, boolean isDebug) {
        this.channel = channel;
        this.token = token;
        this.appName = appName;
        this.className = className;
        this.isDebug = isDebug;
    }

    @Async
    public SendResponse info(String message) {

        if (!this.isDebug)
            return null;
        
        SendResponse response = null;
        try {
            TelegramBot bot = new TelegramBot(token);

            response = bot.execute(new SendMessage(channel, String.format("<b>Приложение</b>: %s\n<b>Class</b>: %s\n<b>Message</b>:\n %s",
                    appName,
                    className,
                    message
            )).parseMode(ParseMode.HTML));
        } catch (Exception e) {

        }

        return response;
    }

}

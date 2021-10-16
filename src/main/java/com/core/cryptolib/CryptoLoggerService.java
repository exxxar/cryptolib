/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.util.Date;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author SAMS
 */
public class CryptoLoggerService {

    protected String application;
    protected String className;
    protected Logger logger;
    protected boolean isDebug;

    public CryptoLoggerService(String application, String className, Logger log) {
        this.isDebug = true;
        this.logger = log;
        this.application = application;
        this.className = className;
    }

    public void setDebugMode(boolean isDebug) {
        this.isDebug = isDebug;
    }

    private String prepare(String message, String payload, String type) {
        JSONObject obj = new JSONObject();
        obj.put("application", application);
        obj.put("class", className);
        obj.put("message", message);
        obj.put("type", type);
        if (payload != null) {
            obj.put("content", payload);
        }
        obj.put("created_at", (new Date()).getTime());

//        TelegramBot bot = new TelegramBot("1910811149:AAFUKo7SSFd6kOmGz3ue5SMIEWWckOk9h9s");
//
//        bot.execute(new SendMessage("-1001276040856", obj.toJSONString()));
        return obj.toJSONString();
    }

    public void info(String message, String payload) {

        if (!this.isDebug) {
            return;
        }

        try {

            logger.info(prepare(message, payload, "info"));
        } catch (Exception e) {

        }
    }

    public void info(String message) {
        this.info(message, null);
    }

    public void error(String message, String payload) {

        if (!this.isDebug) {
            return;
        }

        try {
            logger.error(prepare(message, payload, "error"));
        } catch (Exception e) {

        }
    }

    public void error(String message) {
        this.error(message, null);
    }

    public void warn(String message, String payload) {

        if (!this.isDebug) {
            return;
        }

        try {
            logger.warn(prepare(message, payload, "warn"));
        } catch (Exception e) {

        }
    }

    public void warn(String message) {
        this.warn(message, null);
    }

    public void fatal(String message, String payload) {

        if (!this.isDebug) {
            return;
        }

        try {
            logger.fatal(prepare(message, payload, "fatal"));
        } catch (Exception e) {

        }
    }

    public void fatal(String message) {
        this.fatal(message, null);
    }

}

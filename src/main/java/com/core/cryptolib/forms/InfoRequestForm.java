/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class InfoRequestForm implements Serializable{

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String senderUserId;

    @NotNull
    @Size(min = 1, max = 255)
    private String recipientUserId;

    @NotNull
    @Size(min = 1, max = 4096)
    private String data;

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("senderUserId", senderUserId);
        obj.put("recipientUserId", recipientUserId);
        obj.put("data", data);
        return obj;
    }
    
    

    public TransferDataForm getDataInTDF() throws ParseException, UnsupportedEncodingException {

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(getDecodedBase64Data());
        TransferDataForm tdf = new TransferDataForm();
        tdf.setData(obj.get("data").toString());
        tdf.setType(Integer.parseInt(obj.get("type").toString()));

        return tdf;
    }

    public byte[] getDecodedBase64DataInByte() {
        try {
            return Base64.getDecoder().decode(data.getBytes());
        } catch (Exception ex) {
            return data.getBytes();
        }
    }

    public String getDecodedBase64Data() {
        try {
            return new String(Base64.getDecoder().decode(data.getBytes()));
        } catch (Exception ex) {
            return data;
        }
    }

    public InfoRequestForm(Long id, String senderUserId, String recipientUserId, String data) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.recipientUserId = recipientUserId;
        this.data = data;
    }
    
    

    public InfoRequestForm() {
         this.id = null;
        this.senderUserId = null;
        this.recipientUserId = null;
        this.data = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(String recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}

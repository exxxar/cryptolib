/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import com.core.cryptolib.HashMapConverter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import javax.persistence.Convert;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author SAMS
 */
public class TransferForm {

    private Long id;

    private String senderUserId;

    private String recipientUserId;

    private String data;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> status = new JSONObject();

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    public TransferForm(Long id, String senderUserId, String recipientUserId, String data, LocalDateTime createDateTime, LocalDateTime updateDateTime) {
        this.id = id;
        this.senderUserId = senderUserId;
        this.recipientUserId = recipientUserId;
        this.data = data;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
    }

    public TransferForm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public Map<String, Object> getStatus() {
        return status;
    }

    public void setStatus(Map<String, Object> status) {
        this.status = status;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
    
     public JSONObject toJSON() {
        JSONObject tmp = new JSONObject();
        tmp.put("id", id);
        tmp.put("senderUserId", senderUserId);
        tmp.put("recipientUserId", recipientUserId);
        tmp.put("data", data);
        if (status != null) {
            tmp.put("status", status);
        }

        return tmp;
    }
     
        public TransferDataForm getTransferDataForm() throws UnsupportedEncodingException {
        TransferDataForm transferDataForm = new TransferDataForm();
        transferDataForm.setType(getTransferType());

        if (data == null || data.length() <= 3) {
            transferDataForm.setData("");
            return transferDataForm;
        }

        try {
            Base64.getDecoder().decode(data);
            transferDataForm.setDataBase64(data);
        } catch (IllegalArgumentException iae) {
            transferDataForm.setData(data);
        }

        return transferDataForm;
    }
        
        
    public int getTransferType() {
        if (getStatus() == null) {
            return -1;
        }

        try {
            return (int) getStatus().get("type");
        } catch (Exception ex) {
            return -1;
        }

    }

    public int getDataType() throws ParseException {

        try {
            String tmp = new String(Base64.getDecoder().decode(getData()));

            JSONParser parser = new JSONParser();

            JSONObject object = (JSONObject) parser.parse(tmp);
            return Integer.parseInt(object.get("type").toString());
        } catch (Exception ex) {
            return 0;
        }
    }

    public void setStatusType(int type) {

        JSONObject status = new JSONObject();
        status.put("type", type);
        this.status = status;
    }
}

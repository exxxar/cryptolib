/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import org.json.simple.JSONObject;

/**
 *
 * @author SAMS
 */
public class TransferDataForm implements Serializable {

    private int type;
    private String data;

    public TransferDataForm() {
        type = -1;
        data = null;
    }

    public TransferDataForm(TransferDataForm form) {
        this.type = form.type;
        this.data = form.data;
    }

    public TransferDataForm(int type, String base64data) throws UnsupportedEncodingException {
        this.type = type;
        this.data = Base64.getEncoder().encodeToString(base64data.getBytes());

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDataInBase64() {
        return data;
    }

    public byte[] getDataInBytes() throws UnsupportedEncodingException {
        if (org.apache.commons.codec.binary.Base64.isBase64(data)) {
            return Base64.getDecoder().decode(data.getBytes());
        } else {
            return data.getBytes();
        }
    }

    public String getData() throws UnsupportedEncodingException {
        if (data==null)
            return "";
        
        if (org.apache.commons.codec.binary.Base64.isBase64(data)) {
            return new String(Base64.getDecoder().decode(data.getBytes()));
        } else {
            return data;
        }

    }

    public void setData(String data) throws UnsupportedEncodingException {
        System.out.println("DATA 1.1=>" + data);

        if (org.apache.commons.codec.binary.Base64.isBase64(data)) {
            this.data = data;
        } else {
            this.data = Base64.getEncoder().encodeToString(data.getBytes());
        }

        System.out.println("DATA 1.2=>" + this.data);
    }

    public void setData(byte[] data) {

        this.data = new String(data);

        if (org.apache.commons.codec.binary.Base64.isBase64(data)) {
            this.data = new String(data);
        } else {
            this.data = Base64.getEncoder().encodeToString(data);
        }

    }

    public void setDataBase64(String base64data) {
        this.data = base64data;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", type);

        if (!data.isEmpty()) {
            obj.put("data", data);
        }
        return obj;
    }

    public String toBase64JSON() {
        return Base64.getEncoder().encodeToString(toJSON().toJSONString().getBytes());
    }

    public JSONObject toSimpleJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        return obj;
    }

    public String toBase64SimpleJSON() {
        return Base64.getEncoder().encodeToString(toSimpleJSON().toJSONString().getBytes());
    }

}

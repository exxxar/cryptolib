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
public class EncryptedDataForm implements Serializable {

    private String trustedDeviceData;
    private String userData;
    private int type;

    public EncryptedDataForm() {
        type = -1;
        userData = "";
        trustedDeviceData = null;

    }

    public EncryptedDataForm(EncryptedDataForm form) {
        this.type = form.type;
        this.userData = form.userData;
        this.trustedDeviceData = form.trustedDeviceData;
    }

    public EncryptedDataForm(int type, String base64userData, String base64trustedDeviceData) throws UnsupportedEncodingException {
        this.type = type;
        this.trustedDeviceData = base64trustedDeviceData;
        this.userData = base64userData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserDataInBase64() {
        return userData;
    }

    public String getTrustedDeviceDataInBase64() {
        return trustedDeviceData;
    }

    public byte[] getUserDataInBytes() throws UnsupportedEncodingException {
        return Base64.getDecoder().decode(userData.getBytes());
    }

    public byte[] getTrustedDeviceDataInBytes() throws UnsupportedEncodingException {
        if (!trustedDeviceData.isEmpty()) {
            return Base64.getDecoder().decode(trustedDeviceData.getBytes());
        } else {
            return new byte[0];
        }
    }

    public String getTrustedDeviceData() throws UnsupportedEncodingException {
        if (!trustedDeviceData.isEmpty()) {
            return new String(Base64.getDecoder().decode(trustedDeviceData.getBytes()));
        } else {
            return "";
        }
    }

    public String getUserData() throws UnsupportedEncodingException {
        return new String(Base64.getDecoder().decode(userData.getBytes()));
    }

    public void setUserData(String userData) throws UnsupportedEncodingException {
        this.userData = Base64.getEncoder().encodeToString(userData.getBytes());
    }

    public void setTrustedDeviceData(String trustedDeviceData) throws UnsupportedEncodingException {
        this.trustedDeviceData = Base64.getEncoder().encodeToString(trustedDeviceData.getBytes());
    }

    public void setTrustedDeviceData(byte[] trustedDeviceData) {
        this.trustedDeviceData = Base64.getEncoder().encodeToString(trustedDeviceData);;
    }

    public void setData(byte[] userData) {
        this.userData = Base64.getEncoder().encodeToString(userData);;
    }

    public void setUserDataBase64(String base64userData) {
        this.userData = base64userData;
    }

    public void setTrustedDeviceDataBase64(String base64trustedDeviceData) {
        this.trustedDeviceData = base64trustedDeviceData;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        obj.put("userData", userData);

        if (!trustedDeviceData.isEmpty()) {
            obj.put("trustedDeviceData", trustedDeviceData);
        }
        return obj;
    }

    public String toBase64JSON() {
        return Base64.getEncoder().encodeToString(toJSON().toJSONString().getBytes());
    }

    public JSONObject toSimpleJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        if (!trustedDeviceData.isEmpty()) {
            obj.put("trustedDeviceData", trustedDeviceData);
        }
        obj.put("userData", userData);
        return obj;
    }

    public String toBase64SimpleJSON() {
        return Base64.getEncoder().encodeToString(toSimpleJSON().toJSONString().getBytes());
    }

}

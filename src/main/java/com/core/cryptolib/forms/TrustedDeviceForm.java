/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import java.io.UnsupportedEncodingException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author SAMS
 */
public class TrustedDeviceForm {

    private Long id;

    @NotNull
    @Size(min = 1, max = 36)
    private String devicePublicId;

    @NotNull
    @Size(min = 1, max = 36)
    private String deviceFactoryKey;

    @NotNull
    @Size(min = 1, max = 36)
    private String devicePrivateId;

    @NotNull
    @Size(min = 1, max = 36)
    private String deviceActualKey;

    @NotNull
    @Size(min = 1, max = 36)
    private String deviceOldKey;

    @NotNull
    private boolean active = true;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    public String getDevicePublicId() {
        return devicePublicId;
    }

    public void setDevicePublicId(String devicePublicId) {
        this.devicePublicId = devicePublicId;
    }

    public byte[] getDeviceFactoryKeyAsByteArray() {
        return deviceFactoryKey.getBytes();
    }

    public byte[] getDeviceFactoryKeyAsByteArray(String charset) throws UnsupportedEncodingException {
        return deviceFactoryKey.getBytes(charset);
    }
    
      public String getDeviceFactoryKey() {
        return deviceFactoryKey;
    }

    public void setDeviceFactoryKey(String deviceFactoryKey) {
        this.deviceFactoryKey = deviceFactoryKey;
    }

    public String getDevicePrivateId() {
        return devicePrivateId;
    }

    public void setDevicePrivateId(String devicePrivateId) {
        this.devicePrivateId = devicePrivateId;
    }

    public byte[] getDeviceActualKeyAsByteArray(String charset) throws UnsupportedEncodingException {
        return deviceActualKey.getBytes(charset);
    }

    public byte[] getDeviceActualKeyAsByteArray() {
        return deviceActualKey.getBytes();
    }
    
     public String getDeviceActualKey() {
        return deviceActualKey;
    }

    public void setDeviceActualKey(String deviceActualKey) {
        this.deviceActualKey = deviceActualKey;
    }

    public byte[] getDeviceOldKeyAsByteArray(String charset) throws UnsupportedEncodingException {
        return deviceOldKey.getBytes(charset);
    }

    public byte[] getDeviceOldKeyAsByteArray() {
        return deviceOldKey.getBytes();
    }

     public String getDeviceOldKey() {
        return deviceOldKey;
    }
     
    public void setDeviceOldKey(String deviceOldKey) {
        this.deviceOldKey = deviceOldKey;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

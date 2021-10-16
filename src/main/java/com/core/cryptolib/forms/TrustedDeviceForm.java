/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 *
 * @author SAMS
 */
public class TrustedDeviceForm implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5, max = 36)
    private String devicePublicId;

    @Null
    @Size(min = 5, max = 36)
    private String deviceResetKey;

    @NotNull
    @Size(min = 5, max = 36)
    private String deviceFactoryKey;

    @NotNull
    @Size(min = 5, max = 36)
    private String devicePrivateId;

    @NotNull
    @Size(min = 5, max = 36)
    private String deviceActualKey;

    @Null
    @Size(min = 5, max = 36)
    private String deviceOldKey;

    @NotNull
    private boolean active;

    private boolean acceptAutoReset;

    private boolean resetTry;
    
    private boolean multiconnect;

    @NotNull
    @Size(min = 5, max = 255)
    private String description;

    @NotNull
    @Size(min = 1, max = 255)
    private String currentFirmware;

    @NotNull
    private int attempts = 0;

    public String getDevicePublicId() {
        return devicePublicId;
    }

    public void setDevicePublicId(String devicePublicId) {
        this.devicePublicId = devicePublicId;
    }

    public byte[] getDeviceResetKeyAsByteArray() {
        return deviceResetKey.getBytes();
    }

    public byte[] getDeviceResetKeyAsByteArray(String charset) throws UnsupportedEncodingException {
        return deviceResetKey.getBytes(charset);
    }

    public String getDeviceResetKey() {
        return deviceResetKey;
    }

    public void setDeviceResetKey(String deviceResetKey) {
        this.deviceResetKey = deviceResetKey;
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

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getCurrentFirmware() {
        return currentFirmware;
    }

    public void setCurrentFirmware(String currentFirmware) {
        this.currentFirmware = currentFirmware;
    }

    public TrustedDeviceForm(Long id, String devicePublicId, String deviceResetKey, String deviceFactoryKey, String devicePrivateId, String deviceActualKey, String deviceOldKey, String description) {
        this.id = id;
        this.devicePublicId = devicePublicId;
        this.deviceResetKey = deviceResetKey;
        this.deviceFactoryKey = deviceFactoryKey;
        this.devicePrivateId = devicePrivateId;
        this.deviceActualKey = deviceActualKey;
        this.deviceOldKey = deviceOldKey;
        this.description = description;
        this.attempts = 0;
        this.currentFirmware = "";
        this.acceptAutoReset = false;
        this.resetTry = false;
        this.multiconnect = false;
    }

    public TrustedDeviceForm() {
    }

    public String getDeviceFactoryKey() {
        return deviceFactoryKey;
    }

    public void setDeviceFactoryKey(String deviceFactoryKey) {
        this.deviceFactoryKey = deviceFactoryKey;
    }

    public boolean isAcceptAutoReset() {
        return acceptAutoReset;
    }

    public void setAcceptAutoReset(boolean acceptAutoReset) {
        this.acceptAutoReset = acceptAutoReset;
    }

    public boolean isResetTry() {
        return resetTry;
    }

    public void setResetTry(boolean resetTry) {
        this.resetTry = resetTry;
    }

    public boolean isMulticonnect() {
        return multiconnect;
    }

    public void setMulticonnect(boolean multiconnect) {
        this.multiconnect = multiconnect;
    }

    
}

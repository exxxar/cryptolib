/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.components;

/**
 *
 * @author SAMS
 */
public class ConnectionObject {

    private String devicePublicId;

    private String deviceActualKey;

    private String deviceOldKey;

    private String microserviceActualKey;

    private String microserviceOldKey;

     public ConnectionObject(String devicePublicId, String deviceActualKey, String microserviceActualKey ) {
        this.devicePublicId = devicePublicId;
        this.deviceActualKey = deviceActualKey;
        this.deviceOldKey = "";
        this.microserviceActualKey = microserviceActualKey;
        this.microserviceOldKey = "";
    }
     
    public ConnectionObject(String devicePublicId, String deviceActualKey, String deviceOldKey, String microserviceActualKey, String microserviceOldKey) {
        this.devicePublicId = devicePublicId;
        this.deviceActualKey = deviceActualKey;
        this.deviceOldKey = deviceOldKey;
        this.microserviceActualKey = microserviceActualKey;
        this.microserviceOldKey = microserviceOldKey;
    }

    public ConnectionObject() {
        this.devicePublicId = "";
        this.deviceActualKey = "";
        this.microserviceActualKey = "";
        this.microserviceOldKey = "";
    }

    public ConnectionObject(String devicePublicId) {
        this.devicePublicId = devicePublicId;
        this.deviceActualKey = "";
        this.deviceOldKey = "";
        this.microserviceActualKey = "";
        this.microserviceOldKey = "";
    }

    public String getDevicePublicId() {
        return devicePublicId;
    }

    public void setDevicePublicId(String devicePublicId) {
        this.devicePublicId = devicePublicId;
    }

    public String getDeviceActualKey() {
        return deviceActualKey;
    }

    public void setDeviceActualKey(String deviceActualKey) {
        this.deviceActualKey = deviceActualKey;
    }

    public String getMicroserviceActualKey() {
        return microserviceActualKey;
    }

    public void setMicroserviceActualKey(String microserviceActualKey) {
        this.microserviceActualKey = microserviceActualKey;
    }

    public String getMicroserviceOldKey() {
        return microserviceOldKey;
    }

    public void setMicroserviceOldKey(String microserviceOldKey) {
        this.microserviceOldKey = microserviceOldKey;
    }

    public String getDeviceOldKey() {
        return deviceOldKey;
    }

    public void setDeviceOldKey(String deviceOldKey) {
        this.deviceOldKey = deviceOldKey;
    }

}

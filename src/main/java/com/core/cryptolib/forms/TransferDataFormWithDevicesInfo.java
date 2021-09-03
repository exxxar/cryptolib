/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

/**
 *
 * @author SAMS
 */
public class TransferDataFormWithDevicesInfo {

    private TransferDataForm tdf;

    private TrustedDeviceForm sender;

    private TrustedDeviceForm recipient;

    public TransferDataFormWithDevicesInfo(TransferDataForm tdf, TrustedDeviceForm sender, TrustedDeviceForm recipient) {
        this.tdf = tdf;
        this.sender = sender;
        this.recipient = recipient;
    }

    public TransferDataFormWithDevicesInfo() {
    }

    public TransferDataForm getTdf() {
        return tdf;
    }

    public void setTdf(TransferDataForm tdf) {
        this.tdf = tdf;
    }

    public TrustedDeviceForm getSender() {
        return sender;
    }

    public void setSender(TrustedDeviceForm sender) {
        this.sender = sender;
    }

    public TrustedDeviceForm getRecipient() {
        return recipient;
    }

    public void setRecipient(TrustedDeviceForm recipient) {
        this.recipient = recipient;
    }
    
    
}

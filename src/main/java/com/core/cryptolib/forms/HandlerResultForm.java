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
public class HandlerResultForm {

    TransferForm incomingTransfer;
    TransferForm outgoingTransfer;
    String data;

    public HandlerResultForm(TransferForm incoming) {
        this.incomingTransfer = incoming;
        this.data = "";
        this.outgoingTransfer = null;
    }

    public TransferForm getIncomingForm() {
        return incomingTransfer;
    }

    public void setIncomingTransfer(TransferForm incoming) {
        this.incomingTransfer = incoming;
    }

    public TransferForm getOutgoingTransfer() {
        return outgoingTransfer;
    }

    public void setOutgoingTransfer(TransferForm outgoing) {
        this.outgoingTransfer = outgoing;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    
}

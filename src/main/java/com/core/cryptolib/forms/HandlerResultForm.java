/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import java.io.Serializable;
import org.json.simple.JSONObject;

/**
 *
 * @author SAMS
 */
public class HandlerResultForm implements Serializable {

    TransferForm incomingTransfer;
    TransferForm outgoingTransfer;
    String data;
    String payload;

    public HandlerResultForm(TransferForm incoming) {
        this.incomingTransfer = incoming;
        this.data = "";
        this.payload = "";
        this.outgoingTransfer = new TransferForm();

    }

    public HandlerResultForm() {
        this.incomingTransfer = new TransferForm();
        this.data = "";
        this.payload = "";
        this.outgoingTransfer = new TransferForm();
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

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JSONObject toJSON() {
        JSONObject tmp = new JSONObject();
        tmp.put("data", data);
        tmp.put("payload", payload);
        tmp.put("incomingTransfer", incomingTransfer.toJSON());
        tmp.put("outgoingTransfer", outgoingTransfer.toJSON());

        return tmp;
    }

}

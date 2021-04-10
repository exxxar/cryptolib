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
public class ResponseTDPublicIdForm {

    String tdRecipientTrustedDevicePublicId;
    String tdSenderTrustedDevicePublicId;

    public ResponseTDPublicIdForm(String tdRecipientTrustedDevicePublicId, String tdSenderTrustedDevicePublicId) {
        this.tdRecipientTrustedDevicePublicId = tdRecipientTrustedDevicePublicId;
        this.tdSenderTrustedDevicePublicId = tdSenderTrustedDevicePublicId;
    }

    public String getTdRecipientTrustedDevicePublicId() {
        return tdRecipientTrustedDevicePublicId;
    }

    public void setTdRecipientTrustedDevicePublicId(String tdRecipientTrustedDevicePublicId) {
        this.tdRecipientTrustedDevicePublicId = tdRecipientTrustedDevicePublicId;
    }

    public String getTdSenderTrustedDevicePublicId() {
        return tdSenderTrustedDevicePublicId;
    }

    public void setTdSenderTrustedDevicePublicId(String tdSenderTrustedDevicePublicId) {
        this.tdSenderTrustedDevicePublicId = tdSenderTrustedDevicePublicId;
    }

    
}

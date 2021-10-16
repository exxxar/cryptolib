/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author SAMS
 */
public class MultiConnectionForm {

    @Null
    private Long id;

    @NotNull
    private Long trustedDeviceRecipientId;

    @NotNull
    private Long trustedDeviceSenderId;

    @NotNull
    @Size(min = 5, max = 36)
    private String trustedDeviceMultiplyActualKey;

    @NotNull
    @Size(min = 5, max = 36)
    private String trustedDeviceMultiplyOldKey;

    public MultiConnectionForm() {

        this.trustedDeviceRecipientId = null;
        this.trustedDeviceSenderId = null;
        this.trustedDeviceMultiplyActualKey = null;
        this.trustedDeviceMultiplyOldKey = null;
    }

    public MultiConnectionForm(Long trustedDeviceRecipientId, Long trustedDeviceSenderId, String trustedDeviceMultiplyActualKey, String trustedDeviceMultiplyOldKey) {
        this.trustedDeviceRecipientId = trustedDeviceRecipientId;
        this.trustedDeviceSenderId = trustedDeviceSenderId;
        this.trustedDeviceMultiplyActualKey = trustedDeviceMultiplyActualKey;
        this.trustedDeviceMultiplyOldKey = trustedDeviceMultiplyOldKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrustedDeviceRecipientId() {
        return trustedDeviceRecipientId;
    }

    public void setTrustedDeviceRecipientId(Long trustedDeviceRecipientId) {
        this.trustedDeviceRecipientId = trustedDeviceRecipientId;
    }

    public Long getTrustedDeviceSenderId() {
        return trustedDeviceSenderId;
    }

    public void setTrustedDeviceSenderId(Long trustedDeviceSenderId) {
        this.trustedDeviceSenderId = trustedDeviceSenderId;
    }

    public String getTrustedDeviceMultiplyActualKey() {
        return trustedDeviceMultiplyActualKey;
    }

    public void setTrustedDeviceMultiplyActualKey(String trustedDeviceMultiplyActualKey) {
        this.trustedDeviceMultiplyActualKey = trustedDeviceMultiplyActualKey;
    }

    public String getTrustedDeviceMultiplyOldKey() {
        return trustedDeviceMultiplyOldKey;
    }

    public void setTrustedDeviceMultiplyOldKey(String trustedDeviceMultiplyOldKey) {
        this.trustedDeviceMultiplyOldKey = trustedDeviceMultiplyOldKey;
    }

    
}

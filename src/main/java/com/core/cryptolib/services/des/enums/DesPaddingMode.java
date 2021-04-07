/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.services.des.enums;

/**
 *
 * @author SAMS
 */
public enum DesPaddingMode {
    NO_PADDING("NoPadding"), PKCS5_PADDING("PKCS5Padding"), PKCS7_PADDING("PKCS7Padding");
    String value;

    DesPaddingMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

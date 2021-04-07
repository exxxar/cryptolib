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
public enum ChipperMode {
    CBC("CBC"), ECB("ECB"), CTR("CTR");
    String value;

    ChipperMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

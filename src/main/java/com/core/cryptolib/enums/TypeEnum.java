/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.enums;

/**
 *
 * @author SAMS
 */
public enum TypeEnum {
    String(0),
    Boolean(1),
    Integer(2),
    Double(3),
    JSON(4);

    int value;

    TypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

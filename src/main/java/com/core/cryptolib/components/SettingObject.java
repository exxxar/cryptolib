/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.components;

import com.core.cryptolib.enums.TypeEnum;
import java.io.Serializable;

/**
 *
 * @author SAMS
 */
public class SettingObject implements Serializable {

    String key;
    String value;
    TypeEnum type;

    public SettingObject() {
        this.key = "";
        this.value = "";
        this.type = TypeEnum.String;
    }

    public SettingObject(String key, String value) {
        this.key = key;
        this.value = value;
        this.type = TypeEnum.String;
    }

    public SettingObject(String key, String value, TypeEnum type) {
        this.key = key;
        this.value = value;
        this.type = type == null ? TypeEnum.String : type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

}

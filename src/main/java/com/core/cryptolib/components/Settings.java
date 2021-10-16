/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.components;

import com.core.cryptolib.enums.TypeEnum;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author SAMS
 */
public class Settings {

    public List<SettingObject> settings;

    public Settings() {
        this.settings = new LinkedList<>();
    }

    public SettingObject get(String key) {
        SettingObject st = null;

        for (SettingObject item : settings) {
            if (item.getKey().equals(key)) {
                st = item;
                break;
            }
        }

        if (st == null) {
            st = new SettingObject(key, "");
        }

        return st;
    }

    public boolean isExist(String key) {
        boolean isExist = false;
        for (SettingObject item : settings) {
            if (item.getKey().equals(key)) {
                isExist = true;
                break;
            }
        }

        return isExist;
    }

    public void put(String key, String value) {
        SettingObject st = this.get(key);

        st.setKey(key);
        st.setValue(value);

    }
    
      public void put(String key, String value, TypeEnum type) {
        SettingObject st = this.get(key);

        st.setKey(key);
        st.setValue(value);
        st.setType(type);

    }

    public void put(SettingObject st) {
        if (!isExist(st.getKey())) {
            settings.add(st);
        }

    }

    public List<SettingObject> getSettings() {

        return settings;
    }

    public void setSettings(List<SettingObject> settings) {
        this.settings = settings;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.json.simple.JSONObject;

/**
 *
 * @author SAMS
 */
public class ErrorForm {

    @NotNull
    private int type;

    @NotNull
    private JSONObject error;

    public ErrorForm() {
    }

    public ErrorForm(int type, JSONObject error) {
        this.type = type;
        this.error = error;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JSONObject getError() {
        return error;
    }

    public void setError(JSONObject error) {
        this.error = error;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        obj.put("error", error);
        return obj;

    }

}

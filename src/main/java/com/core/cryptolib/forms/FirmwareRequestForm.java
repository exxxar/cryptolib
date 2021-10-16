/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import com.core.cryptolib.enums.FirmwareStatusEnum;
import org.json.simple.JSONObject;

/**
 *
 * @author SAMS
 */
public class FirmwareRequestForm {
    
    private String version;
    
    private long status;
    
    private long offset;

    public FirmwareRequestForm(JSONObject object){
        this.version = (String)object.get("version");
        this.status =   (long)object.get("status");
        this.offset = (long)object.get("offset");
    }

    public String getVersion() {
        return version;
    }

    public long getStatus() {
        return status;
    }

    public long getOffset() {
        return offset;
    }
    
    
}

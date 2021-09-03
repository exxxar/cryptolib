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
    
    private FirmwareStatusEnum status;
    
    private long offset;

    public FirmwareRequestForm(JSONObject object){
        this.version = (String)object.get("version");
        this.status = Enum.valueOf(FirmwareStatusEnum.class, (String)object.get("status"));
        this.offset = Long.parseLong((String)object.get("offset"));
    }

    public String getVersion() {
        return version;
    }

    public FirmwareStatusEnum getStatus() {
        return status;
    }

    public long getOffset() {
        return offset;
    }
    
    
}

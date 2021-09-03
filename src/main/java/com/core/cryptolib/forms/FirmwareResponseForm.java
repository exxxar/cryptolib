/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib.forms;

import com.core.cryptolib.enums.FirmwareOrderEnum;
import org.json.simple.JSONObject;

/**
 *
 * @author SAMS
 */
public class FirmwareResponseForm {

    private FirmwareOrderEnum order;

    private long offset;

    private String filePart;

    private long checksum;

    public FirmwareResponseForm(FirmwareOrderEnum order, long offset, String filePart, long checksum) {
        this.order = order;
        this.offset = offset;
        this.filePart = filePart;
        this.checksum = checksum;
    }

    public FirmwareOrderEnum getOrder() {
        return order;
    }

    public long getOffset() {
        return offset;
    }

    public String getFilePart() {
        return filePart;
    }

    public long getChecksum() {
        return checksum;
    }

    public JSONObject toJSON() {
        JSONObject tmp = new JSONObject();

        tmp.put("order", order);
        tmp.put("offset", order);
        tmp.put("filePart", order);
        tmp.put("checksum", order);

        return tmp;
    }

}

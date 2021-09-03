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
public enum FirmwareOrderEnum {

//    1-приготовиться к приему файла обновления 
//2-часть файла обновления 
//3-контрольная сумма файла обновления
    PREPARE_UPLOAD(1),
    FILE_PART(2),
    CHECKSUM(3);

    int value;

    FirmwareOrderEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}

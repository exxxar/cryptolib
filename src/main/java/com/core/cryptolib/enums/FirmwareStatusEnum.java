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
public enum FirmwareStatusEnum {

//    
//    Текущий статус обновления:
//0- не готов
//1-готовиться к приему файла обновления
//2-готов принять файл обновления
//3-в процессе приема файла обновления
    
    NOT_READY(0),
    PREPARE_UPLOAD_FIRMARE(1),
    READY_UPLOAD_FIRMAWARE(2),
    UPLOADING_FIRMWARE(3);

    int value;

    FirmwareStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}

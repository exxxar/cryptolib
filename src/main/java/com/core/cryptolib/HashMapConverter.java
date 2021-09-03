/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.cryptolib;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.persistence.AttributeConverter;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Override
    public String convertToDatabaseColumn(Map<String, Object> customerInfo) {
 System.out.println("tyet 1");
        if (customerInfo == null) {
            return null;
        }
 System.out.println("tyet 2");
        String customerInfoJson = null;
        ObjectMapper objectMapper = new ObjectMapper();
 System.out.println("tyet 3");
        try {
            customerInfoJson = objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {

        }
 System.out.println("tyet 4");
        return customerInfoJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String customerInfoJSON) {

        System.out.println("tyt 1");
        if (customerInfoJSON == null) {
            return null;
        }
 System.out.println("tyt 2");
        Map<String, Object> customerInfo = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            customerInfo = objectMapper.readValue(customerInfoJSON, Map.class);
        } catch (final IOException e) {

        }
 System.out.println("tyt 3");
        return customerInfo;
    }

}

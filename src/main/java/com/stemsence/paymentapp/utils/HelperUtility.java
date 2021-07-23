package com.stemsence.paymentapp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.internal.Base64;

import java.nio.charset.StandardCharsets;

public class HelperUtility {
    public static String toBase64String(String value){
        byte[] bites = value.getBytes(StandardCharsets.UTF_8);
        return Base64.encode(bites);
    }
    public static String toJson(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.getLocalizedMessage();
            return null;
        }
    }
}

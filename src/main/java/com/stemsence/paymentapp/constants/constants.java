package com.stemsence.paymentapp.constants;

import okhttp3.MediaType;

public class constants {
    public static String BASIC_AUTH_STRING = "Basic";
    public static String AUTHORIZATION_HEADER_STRING = "authorization";
    public static String CACHE_CONTROL_HEADER = "cache-control";
    public static String CACHE_CONTROL_HEADER_VALUE = "no-cache";
    public static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    public static String BEARER_AUTH_STRING = "Bearer";
    public static int QUESTION_PRICE = 1;
    public static int TEACHER_SHARE = 40;
    public static int STEM_SHARE = 30;
    public static int ELC_SHARE = 10;
    public static int DUTY = 20;
    public static String INITIATOR_NAME = "stemscence";
}

package com.example.utils;

import com.example.constants.http.CorsHeaders;
import com.example.constants.http.HttpHeaders;
import com.example.constants.http.HttpMethod;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public final class CorsUtils {
    private CorsUtils() {}

    public static final String ALLOWED_METHODS = Arrays.stream(HttpMethod.values()).map(Enum::name).collect(Collectors.joining(", "));;

    public static void addHeaders(Map<String,String> headers) {
        headers.put(CorsHeaders.ALLOW_ORIGIN, "*");
        headers.put(CorsHeaders.ALLOW_HEADERS, HttpHeaders.CONTENT_TYPE);
        headers.put(CorsHeaders.ALLOW_METHODS, ALLOWED_METHODS);
    }
}

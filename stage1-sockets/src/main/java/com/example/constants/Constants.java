package com.example.constants;

import java.util.Map;

public class Constants {

    // http headers
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String LOCATION_HEADER = "Location";

    // mime types
    public static final String APPLICATION_JSON_UTF8 = "application/json; charset=utf-8";
    public static final String TEXT_PLAIN_UTF8 = "text/plain; charset=utf-8";

    // http states
    public static final int OK_STATUS = 200;
    public static final int CREATED_STATUS = 201;
    public static final int NO_CONTENT_STATUS = 204;
    public static final int NOT_FOUND_STATUS = 404;
    public static final int INTERNAL_SERVER_ERROR_STATUS = 501;

    public static final Map<Integer, String> STATUS_TEXT_BY_CODE = Map.of(
            OK_STATUS, "OK",
            CREATED_STATUS, "Created",
            NO_CONTENT_STATUS, "No content",
            NOT_FOUND_STATUS, "Not Found",
            INTERNAL_SERVER_ERROR_STATUS, "Internal Server Error"
    );


    // note keys
    public static final String NOTE_ID = "id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_CONTENT = "content";
    public static final String NOTE_CREATEDAT = "createdAt";
}

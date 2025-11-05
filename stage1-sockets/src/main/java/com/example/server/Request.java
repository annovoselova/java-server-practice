package com.example.server;

import com.example.constants.http.HttpMethod;
import com.example.models.Note;
import com.example.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Представляет HTTP-запрос, полученный сервером.
 */
public class Request {

    private HttpMethod method;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private String body;

    public Request(HttpMethod method, String path, Map<String, String> headers, String body) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Path cannot be null ");
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must start with '/'");
        }

        if (!path.equals("/") && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        this.method = method;
        this.path = path;
        this.headers = headers;
        this.body = body;
        this.queryParams =  new HashMap<>();;
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void addQueryParam(String key, String value) {
       queryParams.put(key, value);
    }

    public String getBody() {
        return body;
    }

    public Note getNoteFromBody() {
        return JsonUtils.fromJson(this.body, Note.class);
    }
}
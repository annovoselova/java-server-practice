package com.example.controllers;

import com.example.constants.http.HttpHeaders;
import com.example.constants.http.HttpMimeTypes;
import com.example.constants.http.HttpStatus;
import com.example.server.Response;
import com.example.server.Router;

import java.util.HashMap;
import java.util.Map;

import static com.example.constants.http.HttpMethod.*;

/**
 * Контроллер для запросов о состоянии сервера (Note)
 *
 */
public class HealthController {

    public void register(Router router) {
        router.register(GET, "/health", request -> getHealth());
    }

    /**
     * Возвращает статус сервера в формате JSON.
     *
     * @return объект Router.Result с HTTP-статусом и JSON-телом
     */
    public Response getHealth() {
        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(HttpHeaders.CONTENT_TYPE, HttpMimeTypes.APPLICATION_JSON_UTF8);
        return new Response(HttpStatus.OK, responseHeaders, Map.of("status", "ok"));
    }
}

package com.example.sockets;

import java.util.Map;

public class Router {
    /**
     * Должен вернуть объект ответа: status, headers, body (JSON-строка).
     * Подсказка: реализуйте CRUD /notes и /health согласно openapi.
     */
    public static Result route(String method, String path, Map<String,String> headers, String bodyJson) {
        // TODO: реализовать маршрутизацию
        throw new UnsupportedOperationException("TODO: implement Router.route");
    }

    public record Result(int status, Map<String,String> headers, String body) {}
}

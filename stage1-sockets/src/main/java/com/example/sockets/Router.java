package com.example.sockets;

import com.example.constants.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.controllers.NoteController.*;

/**
 * Класс для маршрутизации http-запросов
 */
public class Router {

    public static Router.Result route(Request request) {
        return route(request.method, request.path, request.headers, request.body);
    }


    /**
     * Отправляет запрос на обработку, в зависимости от аргументов
     *
     * @param method http-метод запроса
     * @param path путь запроса
     * @param headers - заголовки в виде мапы ключ-значение
     * @param bodyJson - тело запроса в виде строки
     * @return объект {@link Router.Result} c кодом статуса, заголовками и телом ответа
     */
    public static Router.Result route(String method, String path, Map<String,String> headers, String bodyJson) {
        if (!path.equals("/") && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if ("/health".equals(path) && "GET".equals(method)) {
            return getHealth();
        }

        if ("/notes".equals(path)) {
            switch (method) {
                case "GET" : return getNotes();
                case "POST" : return createNote(bodyJson);
            }
        }

        String regex = "/notes/(.+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(path);
        if (matcher.matches()) {
            String queryString = matcher.group(1);
            switch (method) {
                case "GET":
                    return getNoteById(queryString);
                case "PUT":
                    return updateNote(queryString, bodyJson);
                case "DELETE":
                    return deleteNote(queryString);
            }
        }
        return notFound();
    }

    public static Router.Result notFound() {
        return error(Constants.NOT_FOUND_STATUS);
    }

    public static Router.Result internalServerError() {
        return error(Constants.INTERNAL_SERVER_ERROR_STATUS);
    }

    private static Router.Result error(int status) {
        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(Constants.CONTENT_TYPE_HEADER, Constants.APPLICATION_JSON_UTF8);
        String body = "error : " + Constants.STATUS_TEXT_BY_CODE.get(status);
        return new Router.Result(status, responseHeaders, body);
    }

    public record Request(String method, String path, Map<String, String> headers, String body) {};
    public record Result(int status, Map<String,String> headers, String body) {}
}

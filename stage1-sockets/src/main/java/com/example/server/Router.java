package com.example.server;

import com.example.constants.http.HttpHeaders;
import com.example.constants.http.HttpMethod;
import com.example.constants.http.HttpMimeTypes;
import com.example.constants.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Класс для маршрутизации http-запросов
 */
public class Router {

    private static class Route {
        final HttpMethod method;
        final Pattern pathPattern;
        final Function<Request, Response> handler;
        final String paramName;

        Route(HttpMethod method, Pattern pathPattern, String paramName, Function<Request, Response> handler) {
            this.method = method;
            this.pathPattern = pathPattern;
            this.handler = handler;
            this.paramName = paramName;
        }
    }

    private final List<Route> routes = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger();

    public void register(HttpMethod method, String pathTemplate, Function<Request, Response> handler) {
        String pathRegex = pathTemplate.replaceAll("\\{[^/]+}", "([^/]+)");
        Pattern pattern = Pattern.compile(pathRegex);
        routes.add(new Route(method, pattern, extractFirstParamName(pathTemplate), handler));
    }

    @Nullable
    private String extractFirstParamName(String pathTemplate) {
        List<String> names = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{([^/]+)}").matcher(pathTemplate);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }


    /**
     * Отправляет запрос на обработку, в зависимости от аргументов
     *
     * @param request - объект с параметрами запроса
     * @return объект {@link Response} c кодом статуса, заголовками и телом ответа
     */
    public Response route(Request request) {

        if (request.getMethod().equals(HttpMethod.OPTIONS)) {
            return corsNoContent();
        }

        String path = request.getPath();
        if (!path.equals("/") && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        for (Route route : routes) {
            if (request.getMethod() == route.method) {
                Matcher matcher = route.pathPattern.matcher(path);
                if (matcher.matches()) {
                    if (matcher.groupCount() == 1) {
                        String pathParam = matcher.group(1);
                        request.addQueryParam(route.paramName, pathParam);
                    }
                    return route.handler.apply(request);
                }
            }
        }

        logger.warn("Маршрут не найден: {} {}", request.getMethod(), request.getPath());
        return notFound();
    }

    private Response corsNoContent() {
        return new Response(HttpStatus.NO_CONTENT, new HashMap<>(), null);
    }

    public static Response notFound() {
        return error(HttpStatus.NOT_FOUND);
    }

    public static Response badRequest() {
        return error(HttpStatus.BAD_REQUEST);
    }

    public static Response internalServerError() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static Response error(HttpStatus status) {
        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(HttpHeaders.CONTENT_TYPE, HttpMimeTypes.APPLICATION_JSON_UTF8);
        return new Response(status, responseHeaders, Map.of("error", status.getMessage()));
    }
}

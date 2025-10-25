package com.example.sockets;

import com.example.constants.Constants;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Главный класс приложения, запускающий HTTP-сервер на сокетах
 */
public class Main {

    /**
     * Создаёт серверный сокет на порту 8080, слушает входящие HTTP-запросы и отправляет на обработку
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try (var ss = new ServerSocket(8080)) {
            System.out.println("Sockets server http://localhost:8080");
            while (true) {
                try (var s = ss.accept();
                     var in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                     var out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {

                    try {
                        Router.Request request = readRequest(in);
                        if (request != null) {
                            Router.Result result = Router.route(request);
                            writeResponse(out, result);
                        }
                    } catch (Exception e) {
                        Router.Result result = Router.internalServerError();
                        writeResponse(out, result);
                        System.err.println("Ошибка при обработке запроса" + e.getMessage());
                    }
                }
            }
        }
    }



    @Nullable
    private static Router.Request readRequest(BufferedReader in) throws IOException {
        String requestLine = in.readLine();
        if (requestLine == null || requestLine.isEmpty()) return null;

        String[] httpRequestParts = requestLine.split(" ");

        String method = httpRequestParts[0];
        String path = httpRequestParts[1];

        Map<String, String> headers = new HashMap<>();
        String line = in.readLine();
        while(line != null && !line.isEmpty()) {
            String[] curHeader = line.split(": ");
            headers.put(curHeader[0], curHeader[1]);
            line = in.readLine();
        }

        int contentLength =  headers.containsKey(Constants.CONTENT_LENGTH_HEADER) ? Integer.parseInt(headers.get(Constants.CONTENT_LENGTH_HEADER)) : 0;
        char[] byteBody = null;
        if (contentLength > 0) {
            byteBody = new char[contentLength];
            in.read(byteBody, 0, contentLength);
        }
        String body = byteBody != null ? new String(byteBody) : null;

        return new Router.Request(method, path, headers, body);
    }



    private static void writeResponse(BufferedWriter out, Router.Result result) throws IOException {
        String status = result.status() + " " + Constants.STATUS_TEXT_BY_CODE.get(result.status());
        out.write("HTTP/1.1 " + status + "\r\n");
        out.write(formatHeader("Date",  new Date().toString()));
        if (result.headers() != null) {
            for (var header : result.headers().entrySet()) {
                out.write(formatHeader(header.getKey(), header.getValue()));
            }
        }

        if (result.body() != null) {
            byte[] byteBody = result.body().getBytes();
            out.write(formatHeader(Constants.CONTENT_LENGTH_HEADER, byteBody.length));
            out.write("\r\n");
            out.write(result.body());
        } else {
            out.write(formatHeader(Constants.CONTENT_LENGTH_HEADER, 0));
        }
        out.flush();
    }

    private static String formatHeader(String key, String value) {
        return key + ": " + value + "\r\n";
    }
    private static String formatHeader(String key, int value) {
        return key + ": " + String.valueOf(value) + "\r\n";
    }
}

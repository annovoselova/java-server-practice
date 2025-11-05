package com.example.server;

import com.example.config.Config;
import com.example.config.ConfigKeys;
import com.example.constants.http.HttpHeaders;
import com.example.constants.http.HttpMethod;
import com.example.exceptions.InvalidNoteException;
import com.example.exceptions.NoteNotFoundException;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Однопоточный HTTP-сервер.
 */
public class Server {

    private final Router router;
    private final Config config;
    private static final Logger logger = LogManager.getLogger();

    public Server(Config config, Router router) {
        this.router = router;
        this.config = config;
    }

    /**
     * Создаёт {@link ServerSocket}, принимает входящие подключения и обрабатывает HTTP-запросы.
     */
    public void start() {
        int port = config.getIntProperty(ConfigKeys.SERVER_PORT);

        try (var ss = new ServerSocket(port)) {
            logger.info("Sockets server http://localhost:" + port);
            while (true) {
                try (var s = ss.accept();
                     var in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                     var out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {

                    try {
                        Request request = readRequest(in);
                        if (request != null) {
                            Response response = router.route(request);
                            writeResponse(out, response);
                        }
                    } catch (NoteNotFoundException e) {
                        writeResponse(out, Router.notFound());
                        logger.warn("Заметка с указанным id не найдена: {}", e.getMessage());
                    } catch (IllegalArgumentException | InvalidNoteException e) {
                        writeResponse(out, Router.badRequest());
                        logger.warn("Невалидный запрос: {}", e.getMessage());
                    } catch (Exception e) {
                        writeResponse(out, Router.internalServerError());
                        logger.error("Ошибка при обработке запроса", e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка во время при запуске сервера", e);
        }
    }

    @Nullable
    private static Request readRequest(BufferedReader in) throws IOException {
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

        int contentLength =  headers.containsKey(HttpHeaders.CONTENT_LENGTH) ? Integer.parseInt(headers.get(HttpHeaders.CONTENT_LENGTH)) : 0;
        char[] byteBody = null;
        if (contentLength > 0) {
            byteBody = new char[contentLength];
            in.read(byteBody, 0, contentLength);
        }
        String body = byteBody != null ? new String(byteBody) : null;
        return new Request(HttpMethod.fromString(method), path, headers, body);
    }



    private static void writeResponse(BufferedWriter out, Response response) throws IOException {
        String status = response.status().getCode() + " " + response.status().getMessage();
        out.write("HTTP/1.1 " + status + "\r\n");
        out.write(formatHeader("Date",  new Date().toString()));
        if (response.headers() != null) {
            for (var header : response.headers().entrySet()) {
                out.write(formatHeader(header.getKey(), header.getValue()));
            }
        }

        if (response.body() != null) {
            byte[] byteBody = response.bodyAsString().getBytes();
            out.write(formatHeader(HttpHeaders.CONTENT_LENGTH, byteBody.length));
            out.write("\r\n");
            out.write(response.bodyAsString());
        } else {
            out.write(formatHeader(HttpHeaders.CONTENT_LENGTH, 0));
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

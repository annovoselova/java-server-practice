package com.example.sockets;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try (var ss = new ServerSocket(8080)) {
            System.out.println("Sockets server http://localhost:8080");
            while (true) {
                try (var s = ss.accept();
                     var in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                     var out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {

                    // TODO: распарсить request line: METHOD PATH HTTP/1.1
                    // TODO: считать заголовки, извлечь Content-Length
                    // TODO: прочитать тело (если есть)
                    // TODO: вызвать Router.route(method, path, headers, body)

                    // Временная заглушка (чтобы сервер стартовал):
                    out.write("HTTP/1.1 501 Not Implemented\r\n");
                    out.write("Content-Length: 0\r\n");
                    out.write("Connection: close\r\n\r\n");
                    out.flush();
                }
            }
        }
    }
}

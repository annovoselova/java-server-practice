package com.example.controller;

import com.example.constants.http.HttpMimeTypes;
import com.example.utils.JsonUtils;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

public class HealthServlet extends HttpServlet {

    public static final String NAME = "HealthServlet";
    public static final String PATH = "/health";

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(HttpMimeTypes.APPLICATION_JSON_UTF8);
        resp.getWriter().write(JsonUtils.toJson(Map.of("status", "ok")));
    }
}

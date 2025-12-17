package com.example.controller;

import com.example.constants.HttpMimeTypes;
import com.example.utils.JsonUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Map;

/** Остальные сервлеты реализовать по типу этого **/
/** GET /health → {"status":"ok"} */
@WebServlet("/health")
public class HealthServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(HttpMimeTypes.APPLICATION_JSON_UTF8);
        resp.getWriter().write(JsonUtils.toJson(Map.of("status", "ok")));
    }
}

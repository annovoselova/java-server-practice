package com.example.war;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/** Остальные сервлеты реализовать по типу этого **/
/** GET /health → {"status":"ok"} */
@WebServlet("/health")
public class HealthServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // TODO: вернуть 200 + JSON
        resp.setStatus(501); // заглушка
    }
}

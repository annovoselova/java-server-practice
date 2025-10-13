package com.example.war;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

/** Инициализация хранилища в ServletContext */
@WebListener
public class AppBootstrap implements ServletContextListener {
    @Override public void contextInitialized(ServletContextEvent sce) {
        // TODO: инициализировать in-memory DB и положить в контекст
    }
}

package com.example.embedded;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/** Запуск embedded Tomcat на 8080 и регистрация сервлетов. */
public class EmbeddedTomcat {
    public static void main(String[] args) throws Exception {
        var tomcat = new Tomcat();
        tomcat.setPort(8080);
        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());

        // TODO: добавить сервлеты (health, notes, notes/*) через Tomcat.addServlet + addServletMappingDecoded

        tomcat.start();
        System.out.println("Embedded Tomcat http://localhost:8080");
        tomcat.getServer().await();
    }
}

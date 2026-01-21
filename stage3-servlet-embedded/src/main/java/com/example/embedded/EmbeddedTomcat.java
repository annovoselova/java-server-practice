package com.example.embedded;

import com.example.config.Config;
import com.example.config.ConfigKeys;
import com.example.controller.HealthServlet;
import com.example.controller.NoteServlet;
import com.example.repository.NoteRepository;
import com.example.services.NoteService;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;


/** Запуск embedded Tomcat на 8080 и регистрация сервлетов. */
public class EmbeddedTomcat {
    public static void main(String[] args) throws Exception {
        Config config = new Config("config.properties");
        int port = config.getIntProperty(ConfigKeys.SERVER_PORT);

        var tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());
        resisterSerlvets(ctx);


        tomcat.start();
        System.out.println("Embedded Tomcat http://localhost:" + port);
        tomcat.getServer().await();
    }

    private static void resisterSerlvets(Context ctx) {
        Tomcat.addServlet(ctx, HealthServlet.NAME, new HealthServlet());
        ctx.addServletMappingDecoded(HealthServlet.PATH, HealthServlet.NAME);

        NoteRepository noteRepository = new NoteRepository();
        NoteService noteService = new NoteService(noteRepository);
        Tomcat.addServlet(ctx, NoteServlet.NAME, new NoteServlet(noteService));
        ctx.addServletMappingDecoded(NoteServlet.PATH, NoteServlet.NAME);
    }
}

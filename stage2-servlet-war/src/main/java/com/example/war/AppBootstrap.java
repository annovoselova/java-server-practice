package com.example.war;

import com.example.constants.ServletContextKeys;
import com.example.repository.NoteRepository;
import com.example.services.NoteService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;

/** Инициализация хранилища в ServletContext */
@WebListener
public class AppBootstrap implements ServletContextListener {
    @Override public void contextInitialized(ServletContextEvent sce) {

        NoteRepository noteRepository = new NoteRepository();
        NoteService noteService = new NoteService(noteRepository);

        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute(ServletContextKeys.NOTE_REPOSITORY, noteRepository);
        ctx.setAttribute(ServletContextKeys.NOTE_SERVICE, noteService);
    }
}

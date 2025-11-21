package com.example.server;

import com.example.controllers.HealthController;
import com.example.controllers.NoteController;
import com.example.repository.NoteRepository;
import com.example.services.NoteService;
import com.example.config.Config;


/**
 * Главный класс приложения, запускающий HTTP-сервер на сокетах
 */
public class Main {

    /**
     * Точка входа в серверное приложение заметок.
     * <p>
     * Инициализирует репозиторий, сервис, контроллеры и маршрутизатор,
     * загружает конфигурацию из файла {@code config.properties}
     * и запускает HTTP-сервер для обработки rest-запросов
     *
     * @param args
     */
    public static void main(String[] args) {

        NoteRepository noteRepository = new NoteRepository();
        NoteService noteService = new NoteService(noteRepository);
        NoteController noteController = new NoteController(noteService);
        HealthController healthController = new HealthController();

        Router router = new Router();
        noteController.register(router);
        healthController.register(router);

        Config config = new Config("config.properties");
        Server server = new Server(config, router);
        server.start();
    }
}

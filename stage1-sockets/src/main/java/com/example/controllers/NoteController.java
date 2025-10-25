package com.example.controllers;

import com.example.constants.Constants;
import com.example.models.Note;
import com.example.services.NoteService;
import com.example.sockets.Router;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления заметками (Note)
 *
 * <p>Обеспечивает CRUD-операции: создание, получение, обновление и удаление заметок
 * Также содержит вспомогательный эндпоинт - метод проверки состояния сервера (getHealth)
 * </p>
 */
public class NoteController {

    /**
     * Возвращает статус сервера в формате JSON.
     *
     * @return объект Router.Result с HTTP-статусом и JSON-телом
     */
    public static Router.Result getHealth() {
        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(Constants.CONTENT_TYPE_HEADER, Constants.APPLICATION_JSON_UTF8);
        String body = "{\"status\":\"ok\"}";
        return new Router.Result(200, responseHeaders, body);
    }

    /**
     * Возвращает Заметку по идентификатору
     *
     * @param id идентификатор заметки
     * @return объект Router.Result с HTTP-статусом и JSON-объектом заметки (или 404, если заметка не найдена)
     */
    public static Router.Result getNoteById(String id) {
        Note note = NoteService.getNoteById(id);
        if (note == null) {
            return Router.notFound();
        }

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(Constants.CONTENT_TYPE_HEADER, Constants.APPLICATION_JSON_UTF8);

        Map<String, String> responseBody = new HashMap<>();
        putNoteToBodyMap(note, responseBody);

        return new Router.Result(200, responseHeaders, new Gson().toJson(responseBody));
    }
    /**
     * Возвращает все существующие заметки
     *
     * @return объект Router.Result с HTTP-статусом и JSON-массивом заметок
     */
    public static Router.Result getNotes() {
        List<Note> notes = NoteService.getNotes();

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(Constants.CONTENT_TYPE_HEADER, Constants.APPLICATION_JSON_UTF8);

        List<Map<String, String>> response = new ArrayList<>();

        for (Note note : notes) {
            Map<String, String> noteMap = new HashMap<>();
            putNoteToBodyMap(note, noteMap);
            response.add(noteMap);
        }
        return new Router.Result(200, responseHeaders, new Gson().toJson(response));
    }

    /**
     * Создает заметку
     *
     * @param bodyJson тело http-запроса c параметрами для создания
     * @return объект Router.Result с HTTP-статусом и JSON-объектом созданной заметки
     */
    public static Router.Result createNote(String bodyJson) {
        Map<String, String> body = new Gson().fromJson(bodyJson, new TypeToken<Map<String, Object>>(){}.getType());
        Note note = NoteService.createNote(body.get(Constants.NOTE_TITLE), body.get(Constants.NOTE_CONTENT));

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(Constants.LOCATION_HEADER, "/notes/" + note.getId());
        responseHeaders.put(Constants.CONTENT_TYPE_HEADER, Constants.APPLICATION_JSON_UTF8);

        Map<String, String> responseBody = new HashMap<>();
        putNoteToBodyMap(note, responseBody);
        return new Router.Result(201, responseHeaders, new Gson().toJson(responseBody));
    }

    /**
     * Обновляет заметку по идентификатору
     *
     * @param id идентификатор заметки
     * @param bodyJson тело http-запроса c параметрами для обновления
     * @return объект Router.Result с HTTP-статусом и JSON-объектом обновленной заметки (или 404, если заметка не найдена)
     */
    public static Router.Result updateNote(String id, String bodyJson) {
        Map<String, String> body = new Gson().fromJson(bodyJson, new TypeToken<Map<String, Object>>(){}.getType());
        Note note = NoteService.updateNote(id, body.get(Constants.NOTE_TITLE), body.get(Constants.NOTE_CONTENT));
        if (note == null) {
            return Router.notFound();
        }

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(Constants.LOCATION_HEADER, "/notes/" + note.getId());
        responseHeaders.put(Constants.CONTENT_TYPE_HEADER, Constants.APPLICATION_JSON_UTF8);

        Map<String, String> responseBody = new HashMap<>();
        putNoteToBodyMap(note, responseBody);
        return new Router.Result(200, responseHeaders, new Gson().toJson(responseBody));
    }

    /**
     * Удаляет заметку по идентификатору
     *
     * @param id идентификатор заметки
     * @return объект Router.Result с HTTP-статусом 204 если удалена (и 404, если не найдена)
     */
    public static Router.Result deleteNote(String id) {
        boolean isDeleted = NoteService.deleteNote(id);
        if (!isDeleted) {
            return Router.notFound();
        }

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(Constants.CONTENT_TYPE_HEADER, Constants.TEXT_PLAIN_UTF8);
        int statusCode = Constants.NO_CONTENT_STATUS;
        String body = Constants.STATUS_TEXT_BY_CODE.get(statusCode);
        return new Router.Result(statusCode, responseHeaders, body);

    }

    private static void putNoteToBodyMap(Note note, Map<String, String> body) {
        body.put(Constants.NOTE_ID, note.getId());
        body.put(Constants.NOTE_TITLE, note.getTitle());
        body.put(Constants.NOTE_CONTENT, note.getContent());
        body.put(Constants.NOTE_CREATEDAT, note.getCreatedAt());
    }
}

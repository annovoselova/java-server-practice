package com.example.controllers;

import com.example.constants.http.HttpHeaders;
import com.example.constants.http.HttpMimeTypes;
import com.example.constants.http.HttpStatus;
import com.example.exceptions.NoteNotFoundException;
import com.example.models.Note;
import com.example.services.NoteService;
import com.example.server.Response;
import com.example.server.Router;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.constants.http.HttpMethod.*;

/**
 * Контроллер для управления заметками (Note).
 *
 * <p>Обеспечивает CRUD-операции: создание, получение, обновление и удаление заметок
 * Также содержит вспомогательный эндпоинт - метод проверки состояния сервера (getHealth)
 * </p>
 */
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    public void register(Router router) {
        router.register(GET, "/notes", request -> getNotes());
        router.register(POST, "/notes", request -> createNote(request.getNoteFromBody()));
        router.register(GET, "/notes/{id}", request -> getNoteById(request.getQueryParams().get("id")));
        router.register(PUT, "/notes/{id}", request -> updateNote(request.getQueryParams().get("id"), request.getNoteFromBody()));
        router.register(DELETE, "/notes/{id}", request -> deleteNote(request.getQueryParams().get("id")));
    }

    /**
     * Возвращает Заметку по идентификатору.
     *
     * @param id идентификатор заметки
     * @return объект Router.Result с HTTP-статусом и JSON-объектом заметки
     * @throws NoteNotFoundException если заметка с указанным идентификатором не найдена
     */
    public Response getNoteById(String id) {
        Note note = noteService.getNoteById(id);

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(HttpHeaders.CONTENT_TYPE, HttpMimeTypes.APPLICATION_JSON_UTF8);

        return new Response(HttpStatus.OK, responseHeaders, note);
    }
    /**
     * Возвращает все существующие заметки.
     *
     * @return объект Router.Result с HTTP-статусом и JSON-массивом заметок
     */
    public Response getNotes() {
        List<Note> notes = noteService.getNotes();

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(HttpHeaders.CONTENT_TYPE, HttpMimeTypes.APPLICATION_JSON_UTF8);
        List<Map<String, String>> response = new ArrayList<>();

        return new Response(HttpStatus.OK, responseHeaders, notes);
    }

    /**
     * Создает заметку.
     *
     * @param noteTemplate объект Note c параметрами для создания
     * @return объект Router.Result с HTTP-статусом и JSON-объектом созданной заметки
     */
    public Response createNote(Note noteTemplate) {
        Note note = noteService.createNote(noteTemplate);

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(HttpHeaders.LOCATION, "/notes/" + note.id());
        responseHeaders.put(HttpHeaders.CONTENT_TYPE, HttpMimeTypes.APPLICATION_JSON_UTF8);

        return new Response(HttpStatus.CREATED, responseHeaders, note);
    }

    /**
     * Обновляет заметку по идентификатору.
     *
     * @param id идентификатор заметки
     * @param noteTemplate объект Note c параметрами для создания
     * @return объект Router.Result с HTTP-статусом и JSON-объектом обновленной заметки
     * @throws NoteNotFoundException если заметка с указанным идентификатором не найдена
     */
    public Response updateNote(String id, Note noteTemplate) {
        Note note = noteService.updateNote(id, noteTemplate);

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(HttpHeaders.LOCATION, "/notes/" + note.id());
        responseHeaders.put(HttpHeaders.CONTENT_TYPE, HttpMimeTypes.APPLICATION_JSON_UTF8);

        return new Response(HttpStatus.OK, responseHeaders, note);
    }

    /**
     * Удаляет заметку по идентификатору.
     *
     * @param id идентификатор заметки
     * @return объект Router.Result с HTTP-статусом 204 если удалена (и 404, если не найдена)
     */
    public Response deleteNote(String id) {
        noteService.deleteNote(id);

        Map<String,String> responseHeaders = new HashMap<>();
        responseHeaders.put(HttpHeaders.CONTENT_TYPE, HttpMimeTypes.APPLICATION_JSON_UTF8);
        return new Response(HttpStatus.NO_CONTENT, responseHeaders, Map.of("error", HttpStatus.NO_CONTENT.getMessage()));
    }
}

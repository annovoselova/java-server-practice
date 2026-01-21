package com.example.controller;

import com.example.constants.http.HttpHeaders;
import com.example.constants.http.HttpMimeTypes;
import com.example.exceptions.NoteNotFoundException;
import com.example.models.Note;
import com.example.services.NoteService;
import com.example.utils.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

public class NoteServlet extends HttpServlet {

    public static final String NAME = "NoteServlet";
    public static final String PATH = "/notes/*";

    private final NoteService noteService;

    public NoteServlet(NoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(HttpMimeTypes.APPLICATION_JSON_UTF8);

        String id = extractId(req);
        if (id == null) {
            resp.getWriter().write(JsonUtils.toJson(noteService.getNotes()));
            return;
        }

        Note note;
        try {
            note = noteService.getNoteById(id);
        } catch (NoteNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Note with id " + id + "not found");
            return;
        }

        resp.getWriter().write(JsonUtils.toJson(note));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Note noteTemplate = JsonUtils.fromJson(body, Note.class);
        Note note = noteService.createNote(noteTemplate);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType(HttpMimeTypes.APPLICATION_JSON_UTF8);
        resp.setHeader(HttpHeaders.LOCATION, "/notes/" + note.id());

        resp.getWriter().write(JsonUtils.toJson(note));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = extractId(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No identifier");
            return;
        }

        String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Note noteTemplate = JsonUtils.fromJson(body, Note.class);

        Note note;
        try {
            note = noteService.updateNote(id, noteTemplate);
        } catch (NoteNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Note with ID " + id + " not found");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(HttpMimeTypes.APPLICATION_JSON_UTF8);
        resp.setHeader(HttpHeaders.LOCATION, "/notes/" + note.id());

        resp.getWriter().write(JsonUtils.toJson(note));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = extractId(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No identifier");
            return;
        }


        try {
            noteService.deleteNote(id);
        } catch (NoteNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Note with ID " + id + " not found");
            return;
        }

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.setContentType(HttpMimeTypes.APPLICATION_JSON_UTF8);
    }

    private String extractId(HttpServletRequest req) {
        String path = req.getPathInfo();

        if (path == null || path.equals("/")) {
            return null;
        }
        String[] parts = path.split("/");
        return parts[1];
    }
}

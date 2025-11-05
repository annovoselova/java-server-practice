package com.example.repository;

import com.example.exceptions.NoteNotFoundException;
import com.example.models.Note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteRepository {
    private static final Map<String, Note> notes = new HashMap<>();

    public Note findById(String id) {
        return notes.get(id);
    }

    public Note findByIdOrThrow(String id) {
        Note note = notes.get(id);
        if (note == null) {
            throw new NoteNotFoundException(id);
        }
        return note;
    }

    public List<Note> findAll() {
        return new ArrayList<Note>(notes.values());
    }

    public void deleteById(String id) {
        notes.remove(id);
    }

    public void save(Note note) {
        notes.put(note.id(), note);
    }
}

package com.example.exceptions;

import com.example.models.Note;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String id) {
        super("Note not found: " + id);
    }
}

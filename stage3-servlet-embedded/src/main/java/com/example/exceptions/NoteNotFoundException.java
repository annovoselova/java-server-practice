package com.example.exceptions;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String id) {
        super("Note not found: " + id);
    }
}

package com.example.models;

import com.example.exceptions.InvalidNoteException;

import java.time.LocalDateTime;

/**
 * Модель заметки (Note)
 */
public record Note (
    String id,
    String title,
    String content,
    LocalDateTime createdAt
) {
    public Note (String title, String content) {
        this(null, title, content, null);
    }

    public Note {
        if (title == null || title.isBlank()) {
            throw new InvalidNoteException("Note title must not be null or blank");
        }
    }
}

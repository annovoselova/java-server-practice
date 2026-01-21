package com.example.models;

import com.example.exceptions.InvalidNoteException;

import java.time.LocalDateTime;

public record Note (
        String id,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public Note {
        if (title == null || title.isBlank()) {
            throw new InvalidNoteException("Note title must not be null or blank");
        }
    }
}

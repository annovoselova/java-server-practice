package com.example.models;

/**
 * Модель заметки (Note)
 */
public class Note {

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    /** Уникальный идентификатор заметки */
    private final String id;
    /** Заголовок заметки */
    private String title;
    /** Содержание заметки */
    private String content;
    /** Дата и время создания заметки в формате ISO 8601 */
    private String createdAt;


    public Note(String id, String title, String content, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}

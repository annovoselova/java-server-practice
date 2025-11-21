package com.example.services;

import com.example.exceptions.NoteNotFoundException;
import com.example.repository.NoteRepository;
import com.example.models.Note;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Сервис для работы с заметками (Note).
 *
 * <p>Предоставляет методы для создания, получения, обновления и удаления заметок</p>
 */
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Удаляет заметку по её идентификатору.
     *
     * @param id идентификатор заметки
     * @throws NoteNotFoundException если заметка с указанным идентификатором не найдена
     */
    public void deleteNote(String id) {
        noteRepository.findByIdOrThrow(id);
        noteRepository.deleteById(id);
    }

    /**
     * Обновляет заметку по идентификатору.
     *
     * @param id идентификатор заметки
     * @param noteTemplate объект с параметрами заметки для редактирования
     * @return обновлённый объект {@link Note}
     * @throws NoteNotFoundException если заметка с указанным идентификатором не найдена
     */
    public Note updateNote(String id, Note noteTemplate) {
        Note oldNote = noteRepository.findByIdOrThrow(id);
        noteRepository.deleteById(id);

        Note updatedNote = new Note(id, noteTemplate.title(), noteTemplate.content(), oldNote.createdAt());
        noteRepository.save(updatedNote);
        return updatedNote;
    }

    /**
     * Возвращает заметку по идентификатору.
     *
     * @param id идентификатор заметки
     * @return объект {@link Note}
     * @throws NoteNotFoundException если заметка с указанным идентификатором не найдена
     */
    public Note getNoteById(String id) {
        return noteRepository.findByIdOrThrow(id);
    }

    /**
     * Создаёт заметку.
     *
     * @param noteTemplate объект с параметрами заметки для создания
     * @return созданный объект {@link Note}
     */
    public Note createNote(Note noteTemplate) {
        String id = UUID.randomUUID().toString();
        Note note = new Note(id, noteTemplate.title(), noteTemplate.content(), LocalDateTime.now());
        noteRepository.save(note);
        return note;
    }

    /**
     * Возвращает список всех существующих заметок.
     *
     * @return список объектов {@link Note}
     */
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

}

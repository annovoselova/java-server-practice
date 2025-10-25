package com.example.services;

import org.jetbrains.annotations.Nullable;
import com.example.models.Note;
import java.time.Instant;
import java.util.*;

/**
 * Сервис для работы с заметками (Note)
 *
 * <p>Предоставляет методы для создания, получения, обновления и удаления заметок</p>
 */
public class NoteService {

    private static final Map<String, Note> notes = new HashMap<>();

    /**
     * Удаляет заметку по её идентификатору
     *
     * @param id идентификатор заметки
     * @return true, если заметка была удалена, false если заметка не найдена
     */
    public static boolean deleteNote(String id) {
        Note note = getNoteById(id);
        if (note != null) {
            notes.remove(id);
            return true;
        }
        return false;
    }

    /**
     * Обновляет заметку по идентификатору
     *
     * @param id идентификатор заметки
     * @param title заголовок заметки
     * @param content содержимое заметки
     * @return обновлённый объект {@link Note}, если заметка не найдена - {@code null}
     */
    @Nullable
    public static Note updateNote(String id, String title, String content) {
        Note note = getNoteById(id);
        if (note != null) {
            if (title != null) {
                note.setTitle(title);
            }
            if (content != null) {
                note.setContent(content);
            }
        }
        return note;
    }

    /**
     * Возвращает заметку по идентификатору
     *
     * @param id идентификатор заметки
     * @return объект {@link Note}, если заметка не найдена - {@code null}
     */
    @Nullable
    public static Note getNoteById(String id) {
        return notes.get(id);
    }

    /**
     * Создаёт заметку
     *
     * @param title заголовок заметки
     * @param content содержимое заметки
     * @return созданный объект {@link Note}
     */
    public static Note createNote(String title, String content) {
        String id = UUID.randomUUID().toString();
        Note note = new Note(id, title, content, Instant.now().toString());
        notes.put(id, note);
        return note;
    }
    /**
     * Возвращает список всех существующих заметок
     *
     * @return список объектов {@link Note}
     */
    public static List<Note> getNotes() {
        return new ArrayList<Note>(notes.values());
    }
}

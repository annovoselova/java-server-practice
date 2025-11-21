package com.example.utils;

import com.example.exceptions.InvalidNoteException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * Утилитарный класс для работы с JSON
 */
public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private static final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    /**
     * Преобразует объект Java в строку JSON.
     *
     * @param object объект для сериализации
     * @return строка JSON
     * @throws RuntimeException если сериализация не удалась
     */
    public static String toJson(Object object) {
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed convert to JSON", e);
        }
    }

    /**
     * Преобразует строку JSON в объект указанного класса.
     *
     * @param json JSON строка
     * @param clazz класс
     * @return объект класса {@code clazz}, заполненный данными из JSON
     * @param <T> тип возвращаемого объекта
     * @throws RuntimeException если десериализация не удалась
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            Throwable cause = e.getCause();
            if (cause instanceof InvalidNoteException) {
                throw (InvalidNoteException) cause;
            }
            else{
                throw new RuntimeException("Failed convert from JSON", e);
            }
        }
    }
}

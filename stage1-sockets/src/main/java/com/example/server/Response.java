package com.example.server;

import com.example.constants.http.HttpStatus;
import com.example.utils.JsonUtils;

import java.util.Map;

/**
 * Представляет HTTP-ответ, формируемый сервером.
 * @param status
 * @param headers
 * @param body - может быть объектом любого типа, сериализуется в JSON при отправке клиенту
 */
public record Response(HttpStatus status, Map<String,String> headers, Object body) {

    /**
     * Возвращает тело ответа в виде JSON.
     * @return тело ответа в виде строки JSON или пустую строку, если {@code body == null}
     */
    public String bodyAsString() {
        if (body == null) {
            return "";
        }
        return JsonUtils.toJson(body);
    }
};

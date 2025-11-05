package com.example.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Класс для загрудки конфигурации из файла ресурсов.
 */
public class Config {
    private static final Properties properties = new Properties();

    /**
     * Загружает конфигурацию из указанного файла ресурсов.
     *
     * @param resourceName имя файла конфигурации, например "config.properties"
     * @throws RuntimeException если файл не найден или произошла ошибка чтения
     */
    public Config(String resourceName) {
        final ClassLoader loader = Config.class.getClassLoader();
        try (var config = loader.getResourceAsStream(resourceName)) {
            properties.load(config);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла конфигурации: " + e.getMessage());
        }
    }

    /**
     * Возвращает значение параметра, как целое число
     * @param key
     * @return значение свойства в виде {@code int}
     * @throws NumberFormatException если значение отсутствует или не является числом
     */
    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}

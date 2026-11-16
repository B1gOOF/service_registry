package com.example.serviceregistry.model;

/**
 * Модель DTO внешнего сервиса для проверки номеров
 *
 * @param id - уникальный идентификатор поставщика
 * @param url - базовый URL API внешнего сервиса (должен быть абсолютным)
 * @param apiKey - ключ авторизации у сервиса
 */
public record Provider(String id, String url, String apiKey) {
}

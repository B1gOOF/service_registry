package com.example.serviceregistry.exception;

/**
 * Выбрасывается, когда возникла ошибка чтения или парсинге конфигурации
 */
public class ProviderConfigurationException extends RuntimeException {
    public ProviderConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}

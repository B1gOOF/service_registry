package com.example.serviceregistry.exception;

/**
 * Выбрасывается, когда запрошенный поставщик отсутствует в реестре
 */
public class ProviderNotFoundException extends RuntimeException {
    public ProviderNotFoundException(String message) {
        super(message);
    }
}

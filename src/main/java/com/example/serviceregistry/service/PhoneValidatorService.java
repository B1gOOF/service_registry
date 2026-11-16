package com.example.serviceregistry.service;

import com.example.serviceregistry.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/**
 * Сервис для проверки доступности номера телефона у поставщиков
 */
@Service
@RequiredArgsConstructor
public class PhoneValidatorService {

    private final ProviderRegistry providerRegistry;
    private final RestClient restClient;

    /**
     * Проверяет доступен ли номер телефона у поставщика
     *
     * @param providerId - идентификатор поставщика
     * @param phone - номер телефона
     * @return true, если номер доступен, иначе false
     */
    public boolean isAvailable(String providerId, String phone) {
        Provider provider = providerRegistry.getById(providerId);

        try {
            StatusResponse body = restClient.get()
                    .uri(provider.url() + "/subscribers/{phone}", phone)
                    .header("X-API-KEY", provider.apiKey())
                    .retrieve()
                    .body(StatusResponse.class);

            return body != null && "ok".equals(body.status());
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }

    private record StatusResponse(String status) {}
}

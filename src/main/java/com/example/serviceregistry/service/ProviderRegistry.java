package com.example.serviceregistry.service;

import com.example.serviceregistry.exception.ProviderNotFoundException;
import com.example.serviceregistry.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderRegistry {

    private volatile Map<String, Provider> providerMap = Map.of();
    private final ObjectMapper objectMapper;

    public Provider getById(String id) {
        Provider provider = providerMap.get(id);
        if (provider == null) {
            throw new ProviderNotFoundException("Provider with id: " + id + " not found");
        }
        return provider;
    }

    public void replaceAll(List<Provider> newProviders) {
        providerMap = newProviders.stream()
                .collect(Collectors.toUnmodifiableMap(Provider::id, Function.identity()));
    }

    public void loadFromJsonFile(Path path) {
        try {
            List<Provider> providerList = objectMapper.readValue(
                    path.toFile(),
                    new TypeReference<>() {
                    }
            );
            replaceAll(providerList);
        } catch (Exception e) { // todo: ловлю exception, чтобы обойти типизацию из jacks3
            throw new UncheckedIOException("Failed to load providers from file: " + path, (IOException) e);
        }
    }
}

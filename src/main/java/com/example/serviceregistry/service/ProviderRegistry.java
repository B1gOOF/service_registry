package com.example.serviceregistry.service;

import com.example.serviceregistry.exception.ProviderConfigurationException;
import com.example.serviceregistry.exception.ProviderNotFoundException;
import com.example.serviceregistry.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
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

    public void loadFromInputStream(InputStream inputStream) {
        try {
            List<Provider> providerList = objectMapper.readValue(inputStream, new TypeReference<List<Provider>>() {});
            replaceAll(providerList);
        } catch (JacksonException e) {
            throw new ProviderConfigurationException("Failed to parse providers from input stream", e);
        }
    }

    public void loadFromJsonFile(Path path) {
        try(InputStream is = Files.newInputStream(path)) {
            loadFromInputStream(is);
        } catch (IOException e) {
            throw new ProviderConfigurationException("Failed to open file: " + path, e);
        }
    }
}

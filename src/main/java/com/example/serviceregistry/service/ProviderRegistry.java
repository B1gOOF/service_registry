package com.example.serviceregistry.service;

import com.example.serviceregistry.exception.ProviderNotFoundException;
import com.example.serviceregistry.model.Provider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProviderRegistry {

    private volatile Map<String, Provider> providerMap = Map.of();

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
}

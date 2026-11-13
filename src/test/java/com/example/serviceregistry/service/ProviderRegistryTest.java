package com.example.serviceregistry.service;

import com.example.serviceregistry.exception.ProviderNotFoundException;
import com.example.serviceregistry.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProviderRegistryTest {

    private ProviderRegistry providerRegistry;

    @BeforeEach
    void setProviderRegistry() {
        providerRegistry = new ProviderRegistry();
    }

    @Test
    void shouldReplaceAllProviderAndFindById() {
        Provider p1 = new Provider("provider-1", "url-1", "provider-key-1");
        Provider p2 = new Provider("provider-2", "url-2", "provider-key-2");

        providerRegistry.replaceAll(List.of(p1, p2));

        assertEquals(p1, providerRegistry.getById("provider-1"));
        assertEquals(p2, providerRegistry.getById("provider-2"));
    }

    @Test
    void shouldThrowExceptionWhenProviderNotFound() {
        assertThrows(ProviderNotFoundException.class, () -> providerRegistry.getById("unknown"));
    }
 }

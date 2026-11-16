package com.example.serviceregistry.service;

import com.example.serviceregistry.exception.ProviderNotFoundException;
import com.example.serviceregistry.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProviderRegistryTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ProviderRegistry providerRegistry;

    @BeforeEach
    void setUp() {
        providerRegistry = new ProviderRegistry(objectMapper);
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

    @Test
    void shouldLoadProvidersFromJson(@TempDir Path tempDir) throws IOException {
        String json = """
                [
                    {"id": "prov-1", "url": "url-1", "apiKey": "provider-key-1"},
                    {"id": "prov-2", "url": "url-2", "apiKey": "provider-key-2"}
                ]
                """;

        Path jsonFile = tempDir.resolve("providers.json");
        Files.writeString(jsonFile, json);

        providerRegistry.loadFromJsonFile(jsonFile);

        assertEquals("provider-key-2", providerRegistry.getById("prov-2").apiKey());
        assertEquals("url-1", providerRegistry.getById("prov-1").url());
    }
 }

package com.example.serviceregistry.service;

import com.example.serviceregistry.model.Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class PhoneValidatorServiceTest {

    private PhoneValidatorService service;
    private MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();

        mockRestServiceServer = MockRestServiceServer.bindTo(builder).build();

        ProviderRegistry registry = new ProviderRegistry(new ObjectMapper());
        registry.replaceAll(List.of(
                new Provider("provider-1", "https://url-1", "provider-key-1")
        ));

        service = new PhoneValidatorService(registry, builder.build());
    }

    @Test
    void shouldReturnTrueWhenProviderResponseWith200AndStatusOk() {
        mockRestServiceServer.expect(requestTo("https://url-1/subscribers/123"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-KEY", "provider-key-1"))
                .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

        boolean isAvailable = service.isAvailable("provider-1", "123");

        assertTrue(isAvailable);
        mockRestServiceServer.verify();
    }

    @Test
    void shouldReturnFalseWhenProviderResponseWith404() {
        mockRestServiceServer.expect(requestTo("https://url-1/subscribers/456"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        boolean isAvailable = service.isAvailable("provider-1", "456");

        assertFalse(isAvailable);
        mockRestServiceServer.verify();
    }
}

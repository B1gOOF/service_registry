package com.example.serviceregistry;

import com.example.serviceregistry.service.ProviderRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@SpringBootApplication
public class ServiceRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(ProviderRegistry providerRegistry) {
        return args -> {
            try (InputStream is = new ClassPathResource("providers.json").getInputStream()) {
                providerRegistry.loadFromInputStream(is);
            } catch (Exception e) {
                throw new IllegalStateException("Cannot start app without providers", e);
            }
        };
    }
}

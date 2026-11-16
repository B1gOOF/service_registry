package com.example.serviceregistry.controller;

import com.example.serviceregistry.service.PhoneValidatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для проверки доступности номеров
 */
@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class PhoneAvailableController {

    private final PhoneValidatorService phoneValidatorService;

    @GetMapping("/{providerId}/subscribers/{phone}")
    public ResponseEntity<AvailabilityResponse> checkAvailability(@PathVariable String providerId, @PathVariable String phone) {
        boolean isAvailable = phoneValidatorService.isAvailable(providerId, phone);

        return ResponseEntity.ok(new AvailabilityResponse(isAvailable));
    }

    public record AvailabilityResponse(boolean available){};
}

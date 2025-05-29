package de.yagub.deliverysystem.msprocessmanager.client.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank String username,
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password
) {}

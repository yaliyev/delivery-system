package de.yagub.deliverysystem.msprocessmanager.client.user.model;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {}
